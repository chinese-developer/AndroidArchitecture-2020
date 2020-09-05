package com.app.base.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.base.permission.EasyPermissions;
import com.android.base.utils.android.compat.StatusBarUtil;
import com.app.base.AppContext;
import com.app.base.R;
import com.app.base.router.RouterPath;
import com.app.base.utils.BaseTools;
import com.app.base.utils.SaveImageUtils;
import com.app.base.utils.ShareUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;

import org.jetbrains.annotations.NotNull;

import me.jingbin.web.ByWebView;
import me.jingbin.web.OnTitleProgressCallback;
import timber.log.Timber;

import static com.app.base.AppContextKt.toast;

/**
 * 网页可以处理:
 * 点击相应控件:拨打电话、发送短信、发送邮件、上传图片、播放视频
 * 进度条、返回网页上一层、显示网页标题
 * Thanks to: <a href="https://github.com/youlookwhat/ByWebView"/>
 */
public class WebViewActivity extends AppCompatActivity {

    // title
    private String mTitle;
    // 网页链接
    private String mUrl;
    // 可滚动的title 使用简单 没有渐变效果，文字两旁有阴影
    private TextView toolbarTitle;
    private boolean isTitleFix;
    private ByWebView byWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.red));
        getIntentData();
        initTitle();
        syncCookie(mUrl);
        getDataFromBrowser(getIntent());
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("mTitle");
            mUrl = getIntent().getStringExtra("mUrl");
            isTitleFix = getIntent().getBooleanExtra("isTitleFix", false);
        }
    }

    private void initTitle() {
        LinearLayout llWebView = findViewById(R.id.ll_webview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_actionbar_more));
        toolbarTitle.postDelayed(() -> toolbarTitle.setSelected(true), 1900);
        toolbarTitle.setText(mTitle);

        byWebView = ByWebView.with(this)
                .setWebParent(llWebView, new LinearLayout.LayoutParams(-1, -1))
                .useWebProgress(ContextCompat.getColor(this, R.color.red_level1))
                .setOnTitleProgressCallback(new OnTitleProgressCallback() {
                    @Override
                    public void onReceivedTitle(String title) {
                        setTitle(title);
                    }
                })
                .loadUrl(mUrl);
        byWebView.getWebView().setOnLongClickListener(v -> handleLongImage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }

    public void setTitle(String mTitle) {
        if (!isTitleFix) {
            toolbarTitle.setText(mTitle);
            this.mTitle = mTitle;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {// 返回键
            handleFinish();
        } else if (itemId == R.id.actionbar_share) {// 分享到
            String shareText = mTitle + " " + byWebView.getWebView().getUrl() + " ( 分享自大米音乐 " + /*Constants.DOWNLOAD_URL*/1 + " )";
            ShareUtils.share(WebViewActivity.this, shareText);
        } else if (itemId == R.id.actionbar_cope) {// 复制链接
            BaseTools.copy(byWebView.getWebView().getUrl());
            toast("已复制到剪贴板");
        } else if (itemId == R.id.actionbar_open) {// 打开链接
            BaseTools.openLink(WebViewActivity.this, byWebView.getWebView().getUrl());
        } else if (itemId == R.id.actionbar_webview_refresh) {// 刷新页面
            byWebView.reload();
        } else if (itemId == R.id.actionbar_collect) {// 添加到收藏
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 同步cookie
     */
    private void syncCookie(String url) {
        if (!TextUtils.isEmpty(url) && url.contains("dami_music")) {
            try {
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                String cookie = SPUtils.getInstance().getString("cookie", "");
                if (!TextUtils.isEmpty(cookie)) {
                    String[] split = cookie.split(";");
                    for (String s : split) {
                        cookieManager.setCookie(url, s);
                    }
                }
                cookieManager.flush();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        byWebView.handleFileChooser(requestCode, resultCode, intent);
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    /**
     * 作为三方浏览器打开
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String url = scheme + "://" + host + path;
                byWebView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 长按图片事件处理
     */
    private boolean handleLongImage() {
        final WebView.HitTestResult hitTestResult = byWebView.getWebView().getHitTestResult();
        // 如果是图片类型或者是带有图片链接的类型
        if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // 弹出保存图片的对话框
            new AlertDialog.Builder(WebViewActivity.this)
                    .setItems(new String[]{"发送给朋友", "保存到相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //获取图片
                            String picUrl = hitTestResult.getExtra();
                            switch (which) {
                                case 0:
                                    ShareUtils.shareNetImage(WebViewActivity.this, picUrl);
                                    break;
                                case 1:
                                    if (!EasyPermissions.hasPermissions(WebViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        return;
                                    }
                                    SaveImageUtils.saveImageToGallery(WebViewActivity.this, picUrl, picUrl);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (byWebView.handleKeyEvent(keyCode, event)) {
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                handleFinish();
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 直接通过三方浏览器打开时，回退到首页
     */
    public void handleFinish() {
        supportFinishAfterTransition();
        if (!AppUtils.isAppForeground()) {
            AppContext.appRouter().build(RouterPath.Main.PATH).navigation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        byWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        byWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        byWebView.onDestroy();
        toolbarTitle.clearAnimation();
        toolbarTitle.clearFocus();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    /**
     * 禁止改变字体大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * 打开网页:
     *
     * @param mContext 上下文
     * @param mUrl     要加载的网页url
     * @param mTitle   title
     */
    public static void loadUrl(Context mContext, String mUrl, String mTitle) {
        loadUrl(mContext, mUrl, mTitle, false);
    }

    /**
     * 打开网页:
     *
     * @param mContext     上下文
     * @param mUrl         要加载的网页url
     * @param mTitle       title
     * @param isTitleFixed title是否固定
     */
    public static void loadUrl(Context mContext, String mUrl, String mTitle, boolean isTitleFixed) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("mUrl", mUrl);
        intent.putExtra("isTitleFix", isTitleFixed);
        intent.putExtra("mTitle", mTitle == null ? "" : mTitle);
        mContext.startActivity(intent);
    }

}
