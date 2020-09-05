package com.android.base.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.base.utils.android.compat.AndroidVersion;
import com.github.dmstocking.optional.java.util.function.Predicate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * <pre>
 *          1，封装通用流程。
 *          2，onBackPressed 事件分发，优先交给 Fragment 处理。
 *          3，提供 RxJava 的生命周期绑定。
 *  </pre>
 */
@SuppressWarnings("rawtypes")
public abstract class BaseActivity extends AppCompatActivity implements ActivityDelegateOwner {

    private final ActivityDelegates activityDelegates = new ActivityDelegates(this);
    private ActivityStatus activityStatus = ActivityStatus.INITIALIZED;
    private String tag() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.tag(tag()).d( ">>>> onCreate before call super");

        initialize(savedInstanceState);
        activityDelegates.callOnCreateBeforeSetContentView(savedInstanceState);

        super.onCreate(savedInstanceState);
        Timber.tag(tag()).d( ">>>> onCreate after call super: [bundle= " + savedInstanceState + "]");

        activityStatus = ActivityStatus.CREATE;
    }

    @Override
    protected void onRestart() {
        Timber.tag(tag()).d( ">>>> onRestart before call super");
        super.onRestart();
        Timber.tag(tag()).d( ">>>> onRestart after call super");
        activityDelegates.callOnRestart();
    }

    @Override
    protected void onStart() {
        Timber.tag(tag()).d(">>>> onStart before call super");
        super.onStart();
        Timber.tag(tag()).d( ">>>> onStart after call super");
        activityStatus = ActivityStatus.START;
        activityDelegates.callOnStart();
    }

    @Override
    protected void onResume() {
        Timber.tag(tag()).d( ">>>> onResume before call super");
        super.onResume();
        Timber.tag(tag()).d( ">>>> onResume after call super");
        activityStatus = ActivityStatus.RESUME;
        activityDelegates.callOnResume();
    }

    @Override
    protected void onPause() {
        Timber.tag(tag()).d( ">>>> onPause before call super");
        activityStatus = ActivityStatus.PAUSE;
        activityDelegates.callOnPause();
        super.onPause();
        Timber.tag(tag()).d( ">>>> onPause after call super");
    }

    @Override
    protected void onStop() {
        Timber.tag(tag()).d( ">>>> onStop before call super");
        activityStatus = ActivityStatus.STOP;
        activityDelegates.callOnStop();
        super.onStop();
        Timber.tag(tag()).d( ">>>> onStop after call super");
    }

    @Override
    protected void onDestroy() {
        Timber.tag(tag()).d( ">>>> onDestroy before call super");
        activityStatus = ActivityStatus.DESTROY;
        activityDelegates.callOnDestroy();
        super.onDestroy();
        Timber.tag(tag()).d( ">>>> onDestroy after call super");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Timber.tag(tag()).d( ">>>> onPostCreate: [bundle= " + savedInstanceState + "]");
        activityDelegates.callOnPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.tag(tag()).d( ">>>> onSaveInstanceState: [bundle= " + outState + "]");
        activityDelegates.callOnSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Timber.tag(tag()).d( ">>>> onRestoreInstanceState: [bundle= " + savedInstanceState + "]");
        activityDelegates.callOnRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.tag(tag()).d( ">>>> onActivityResult: [resultCode= " + resultCode + "]");
        activityDelegates.callOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Timber.tag(tag()).d( ">>>> onRequestPermissionsResult: [requestCode= " + requestCode + "]");
        activityDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Timber.tag(tag()).d( ">>>> onResumeFragments");
        activityDelegates.callOnResumeFragments();
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // /
    // interface impl
    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // /
    @UiThread
    @Override
    public final void addDelegate(@NonNull ActivityDelegate activityDelegate) {
        activityDelegates.addActivityDelegate(activityDelegate);
    }

    @SuppressWarnings("unused")
    @UiThread
    @Override
    public final boolean removeDelegate(@NonNull ActivityDelegate activityDelegate) {
        return activityDelegates.removeActivityDelegate(activityDelegate);
    }

    @Override
    public ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate) {
        return activityDelegates.findDelegate(predicate);
    }

    @Override
    public ActivityStatus getStatus() {
        return activityStatus;
    }

    /**
     * Before call super.onCreate and setContentView
     *
     * @param savedInstanceState state
     */
    protected void initialize(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onBackPressed() {
        if (BackHandlerHelper.handleBackPress(this)) {
            Timber.d("onBackPressed() called but child fragment handle it");
        } else {
            superOnBackPressed();
        }
    }

    protected void superOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean isDestroyed() {
        if (AndroidVersion.atLeast(17)) {
            return super.isDestroyed();
        } else {
            return getStatus() == ActivityStatus.DESTROY;
        }
    }

}
