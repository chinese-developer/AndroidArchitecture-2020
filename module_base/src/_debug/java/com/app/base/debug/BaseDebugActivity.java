package com.app.base.debug;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.android.base.permission.AutoPermissionRequester;
import com.android.base.permission.Permission;
import com.android.sdk.net.NetConfig;
import com.app.base.AppContext;
import com.app.base.R;
import com.app.base.data.DataConfig;
import com.app.base.data.app.AppDataSource;
import com.app.base.utils.verify.ValidatorKt;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.joor.Reflect;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.ele.uetool.UETool;
import timber.log.Timber;

import static com.app.base.AppContextKt.toast;

/**
 * 仅用于调试版本
 */
@AndroidEntryPoint
public class BaseDebugActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private TextView mUserTv;
    private EditText mMobilePhoneEt;
    private EditText mPasswordEt;

    @Inject
    AppDataSource mAppDataSource;

    private DebugApi mDebugApi;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_debug);
        initUI();
        initUser();
        showHost(DataConfig.getInstance().hostIdentification());
        requestPermission();
    }

    @SuppressLint("CutPasteId")
    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mUserTv = findViewById(R.id.debug_tv_user);
        mMobilePhoneEt = findViewById(R.id.debug_phone);
        mPasswordEt = findViewById(R.id.debug_password);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 用户相关
    ///////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    private void initUser() {
        mDebugApi = NetConfig.INSTANCE.getRetrofitWithoutToken().create(DebugApi.class);

        mAppDataSource.observableUser()
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(user -> {
                    if (AppContext.appDataSource().isLoggedIn()) {
                        mUserTv.setText("登录已用户：" + user.getDisplayName());
                    } else {
                        mUserTv.setText("没有用户登录");
                    }
                }, Timber::e);


        Button switchBtn = findViewById(R.id.debug_btn_switch);
        switchBtn.setText("显示登录视图");
        View loginView = findViewById(R.id.debug_ll_login_layout);
        switchBtn.setOnClickListener(v -> {
            if (loginView.getVisibility() == View.GONE) {
                loginView.setVisibility(View.VISIBLE);
                switchBtn.setText("隐藏登录视图");
            } else {
                switchBtn.setText("显示登录视图");
                loginView.setVisibility(View.GONE);
            }
        });
    }


    @SuppressWarnings("all")
    public void login(View view) {
        if (!ValidatorKt.validateCellphone(mMobilePhoneEt, false)) {
            return;
        }
        if (!ValidatorKt.validatePassword(mPasswordEt, true, false)) {
            return;
        }

        mProgressDialog.show();

        /*mDebugApi.pwdLogin(
                mMobilePhoneEt.getText().toString(),
                mPasswordEt.getText().toString())
                .compose(RxResult.resultExtractor())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> mProgressDialog.dismiss())
                .subscribe(
                        new Consumer<LoginResponse>() {
                            @Override
                            public void accept(LoginResponse responseModel) throws Exception {
                                mAppDataSource.saveUser(responseModel.getLogin_info(), responseModel.getApp_token(), responseModel.getExpire_time());
                                ToastUtils.showShort("登录成功");
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                ToastUtils.showShort("登录失败：" + throwable);
                            }
                        });*/
    }

    ///////////////////////////////////////////////////////////////////////////
    // 环境相关
    ///////////////////////////////////////////////////////////////////////////

    private void showHost(int hostEnvIdentification) {
        TextView tv = findViewById(R.id.debug_tv_host);
        if (hostEnvIdentification == DataConfig.BUILD_DEV) {
            tv.setText("环境：开发");
        } else if (hostEnvIdentification == DataConfig.BUILD_TEST) {
            tv.setText("环境：测试");
        } else if (hostEnvIdentification == DataConfig.BUILD_RELEASE) {
            tv.setText("环境：正式");
        }
    }

    public void switchHost(View view) {
        int hostEnvIdentification = DataConfig.getInstance().hostIdentification();
        final int[] choice = new int[1];
        if (hostEnvIdentification == DataConfig.BUILD_TEST) {
            choice[0] = 1;
        } else if (hostEnvIdentification == DataConfig.BUILD_RELEASE) {
            choice[0] = 2;
        }
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(new String[]{"开发", "测试", "正式"}, choice[0], (dialog, which) -> {
                    dialog.dismiss();
                    int host;
                    if (which == 0) {
                        host = DataConfig.BUILD_DEV;
                    } else if (which == 1) {
                        host = DataConfig.BUILD_TEST;
                    } else {
                        host = DataConfig.BUILD_RELEASE;
                    }
                    showRestartAction(host);
                }).show();
    }

    private void showRestartAction(int host) {
        new AlertDialog.Builder(this)
                .setMessage("应用将会重启！当前环境的登录状态与数据缓存将会清空，确定要切换吗？")
                .setNegativeButton(R.string.cancel_, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    dialog.dismiss();

                    //修改 host 标识
                    showHost(host);
                    DataConfig.getInstance().switchHost(host);
                    //清除所有数据
                    AppContext.appDataSource().logout();
                    AppContext.storageManager().stableStorage().clearAll();
                    //重启
                    getWindow().getDecorView().post(this::doRestart);

                }).show();
    }

    private void doRestart() {
        AppContext.get().restartApp();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    ///////////////////////////////////////////////////////////////////////////
    // 权限相关
    ///////////////////////////////////////////////////////////////////////////

    private void requestPermission() {
        AutoPermissionRequester.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                .onDenied(strings -> {
                    toast("没有权限");
                    supportFinishAfterTransition();
                }).request();
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 进入app
     */
    public void start(View view) {
        AppContext.get().restartApp();
    }

    public void openUETool(View view) {
        try {
            UETool.showUETMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeUETool(View view) {
        try {
            Reflect.on("me.ele.uetool.UETool").call("dismissUETMenu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restart(View view) {
        doRestart();
    }

}
