package com.android.base.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;

import com.android.base.receiver.NetStateReceiver;
import com.android.base.utils.BaseUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.yc.toollib.network.utils.NetworkTool;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess,unused")
public final class ApplicationDelegate {

    private Application application;

    private BehaviorProcessor<Boolean> appStatus;

    private AtomicBoolean onCreateCalled = new AtomicBoolean(false);
    private AtomicBoolean onAttachBaseCalled = new AtomicBoolean(false);

    ApplicationDelegate() {
    }

    public void attachBaseContext(@SuppressWarnings("unused") Context base) {
        if (!onAttachBaseCalled.compareAndSet(false, true)) {
            throw new IllegalStateException("Can only be called once");
        }
        // no op
    }

    public void onCreate(Application application) {
        if (!onCreateCalled.compareAndSet(false, true)) {
            throw new IllegalStateException("Can only be called once");
        }
        this.application = application;
        // 工具类初始化
        BaseUtils.init(application);
        // 异常日志记录
        CrashHandler.register(application);
        // 网络状态
        listenNetworkState();
        // App前台后台
        listenActivityLifecycleCallbacks();
    }

    public void onTerminate() {
        // no op
    }

    public void onConfigurationChanged(Configuration newConfig) {
        // no op
    }

    public void onTrimMemory(int level) {
        // no op
    }

    public void onLowMemory() {
        // no op
    }

    private void listenNetworkState() {
        NetStateReceiver netStateReceiver = new NetStateReceiver();
        application.registerReceiver(netStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void listenActivityLifecycleCallbacks() {
        appStatus = BehaviorProcessor.create();
        AppUtils.registerAppStatusChangedListener(new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground(Activity activity) {
                Timber.d("app 进入前台");
                appStatus.onNext(true);
            }

            @Override
            public void onBackground(Activity activity) {
                Timber.d("app 进入后台");
                appStatus.onNext(false);
            }
        });
    }

    /**
     * 获取可观察的 app 生命周期
     */
    Flowable<Boolean> appAppState() {
        return appStatus;
    }

    void setCrashProcessor(/*Sword.CrashProcessor crashProcessor*/boolean isOpenDebug) {
        if (isOpenDebug) {
            NetworkTool.getInstance().init(application);
            // 开启悬浮按钮, 点击去网络拦截列表页面查看网络请求数据
            NetworkTool.getInstance().setFloat(application);
        }
    }

    Application getApplication() {
        return application;
    }

}