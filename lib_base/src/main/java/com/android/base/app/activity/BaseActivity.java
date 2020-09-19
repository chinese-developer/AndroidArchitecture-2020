package com.android.base.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.base.TagsFactory;
import com.github.dmstocking.optional.java.util.function.Predicate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * ·
 *          1，封装通用流程。
 *          2，onBackPressed 事件分发，优先交给 Fragment 处理。
 *          3，提供 RxJava 的生命周期绑定。
 *  </pre>
 */
@SuppressWarnings("rawtypes")
public abstract class BaseActivity extends AppCompatActivity implements ActivityDelegateOwner {

    private final ActivityDelegates activityDelegates = new ActivityDelegates(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onCreate before call super");
        activityDelegates.callOnCreateBeforeSetContentView(savedInstanceState);
        super.onCreate(savedInstanceState);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onCreate after call super: [bundle= " + savedInstanceState + "]");
    }

    @Override
    protected void onRestart() {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onRestart before call super");
        super.onRestart();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onRestart after call super");
        activityDelegates.callOnRestart();
    }

    @Override
    protected void onStart() {
        Timber.tag(TagsFactory.activity_lifecycle).i(">>>> onStart before call super");
        super.onStart();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onStart after call super");
        activityDelegates.callOnStart();
    }

    @Override
    protected void onResume() {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onResume before call super");
        super.onResume();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onResume after call super");
        activityDelegates.callOnResume();
    }

    @Override
    protected void onPause() {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onPause before call super");
        activityDelegates.callOnPause();
        super.onPause();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onPause after call super");
    }

    @Override
    protected void onStop() {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onStop before call super");
        activityDelegates.callOnStop();
        super.onStop();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onStop after call super");
    }

    @Override
    protected void onDestroy() {
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onDestroy before call super");
        activityDelegates.callOnDestroy();
        super.onDestroy();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onDestroy after call super");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onPostCreate: [bundle= " + savedInstanceState + "]");
        activityDelegates.callOnPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onSaveInstanceState: [bundle= " + outState + "]");
        activityDelegates.callOnSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onRestoreInstanceState: [bundle= " + savedInstanceState + "]");
        activityDelegates.callOnRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onActivityResult: [resultCode= " + resultCode + "]");
        activityDelegates.callOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onRequestPermissionsResult: [requestCode= " + requestCode + "]");
        activityDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Timber.tag(TagsFactory.activity_lifecycle).i( ">>>> onResumeFragments");
        activityDelegates.callOnResumeFragments();
    }

    @UiThread
    @Override
    public final void addDelegate(@NonNull ActivityDelegate activityDelegate) {
        activityDelegates.addActivityDelegate(activityDelegate);
    }

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

}
