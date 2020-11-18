package com.android.base.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.base.app.ui.LoadingViewFactory;
import com.android.base.interfaces.adapter.ActivityLifecycleCallbacksAdapter;
import com.android.base.receiver.NetworkState;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;

import java.lang.ref.WeakReference;

import io.reactivex.Flowable;

/**
 * 基础库工具
 */
@UiThread
public final class Sword {

    private WeakReference<Activity> currentActivity;
    private static final Sword ONLY_BASE = new Sword();

    private Sword() {
        applicationDelegate = new ApplicationDelegate();
    }

    public static Sword get() {
        return ONLY_BASE;
    }

    /**
     * LoadingView
     */
    private LoadingViewFactory loadingViewFactory;

    /**
     * Application lifecycle delegate
     */
    private ApplicationDelegate applicationDelegate;

    /**
     * 错误类型检查
     */
    private ErrorClassifier errorClassifier;

    /**
     * 获取 Application 代理
     */
    @SuppressWarnings("WeakerAccess")
    public ApplicationDelegate getApplicationDelegate() {
        return applicationDelegate;
    }

    public Sword registerLoadingFactory(LoadingViewFactory loadingViewFactory) {
        if (this.loadingViewFactory != null) {
            throw new UnsupportedOperationException("LoadingViewFactory had already set");
        }
        this.loadingViewFactory = loadingViewFactory;
        return this;
    }

    public LoadingViewFactory getLoadingViewFactory() {
        if (loadingViewFactory == null) {
            throw new NullPointerException("you have not set the LoadingViewFactory by AndroidBase");
        }
        return loadingViewFactory;
    }

    /** subscribe 会存在内存泄漏, 因为是静态的, 所以使用前要保证内存中仅有一处订阅, 且生命周期是全局的 */
    public Flowable<NetworkState> networkState() {
        return NetworkState.observableState();
    }

    public interface CrashProcessor {
        void uncaughtException(Thread thread, Throwable ex);
    }

    public Sword setCrashProcessor(/*CrashProcessor crashProcessor*/boolean isOpenDebug) {
        applicationDelegate.setCrashProcessor(isOpenDebug);
        return this;
    }

    /**
     * 通过订阅Activity、Fragment的生命周期接口的回调，来注入Dagger2.
     * <p>
     */
    public Sword enableAutoInject() {
        Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacksAdapter() {

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (currentActivity != null && currentActivity.get() == activity) {
                    currentActivity = null;
                }
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            private void handedFragmentInject(FragmentActivity activity) {
                activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {

                    }
                }, true);
            }
        };
        applicationDelegate.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        return this;
    }

    /**
     * 获取可观察的 app 生命周期
     */
    public Flowable<Boolean> appState() {
        return applicationDelegate.appAppState();
    }

    /**
     * 获取当前resume的Activity
     *
     * @return activity
     */
    @Nullable
    public Activity getTopActivity() {
        return ActivityUtils.getTopActivity();
    }

    public interface ErrorClassifier {
        boolean isNetworkError(Throwable throwable);

        boolean isServerError(Throwable throwable);
    }

    @SuppressWarnings("all")
    public Sword setErrorClassifier(ErrorClassifier errorClassifier) {
        if (this.errorClassifier != null) {
            throw new UnsupportedOperationException("ErrorClassifier had already set");
        }
        this.errorClassifier = errorClassifier;
        return this;
    }

    public ErrorClassifier errorClassifier() {
        return errorClassifier;
    }

    @Nullable
    public Activity getCurrentActivity() {
        if (currentActivity != null) {
            return currentActivity.get();
        }
        return null;
    }

}