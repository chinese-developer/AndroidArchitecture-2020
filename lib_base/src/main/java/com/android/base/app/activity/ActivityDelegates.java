package com.android.base.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.github.dmstocking.optional.java.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("rawtypes")
@UiThread
final class ActivityDelegates {

    private final List<ActivityDelegate> delegates;
    private AppCompatActivity baseActivity;

    ActivityDelegates(AppCompatActivity baseActivity) {
        delegates = new ArrayList<>(4);
        this.baseActivity = baseActivity;
    }

    void callOnCreateBeforeSetContentView(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onCreateBeforeSetContentView(savedInstanceState);
        }
    }

    void callOnCreateAfterSetContentView(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onCreateAfterSetContentView(savedInstanceState);
        }
    }

    void callOnRestoreInstanceState(Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onRestoreInstanceState(savedInstanceState);
        }
    }

    void callOnPostCreate(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onPostCreate(savedInstanceState);
        }
    }

    void callOnSaveInstanceState(Bundle outState) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    void callOnDestroy() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onDestroy();
        }
    }

    void callOnStop() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onStop();
        }
    }

    void callOnPause() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onPause();
        }
    }

    void callOnResume() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onResume();
        }
    }

    void callOnStart() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onStart();
        }
    }

    void callOnRestart() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onRestart();
        }
    }

    void callOnActivityResult(int requestCode, int resultCode, Intent data) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    void callOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void callOnResumeFragments() {
        for (ActivityDelegate activityDelegate : delegates) {
            activityDelegate.onResumeFragments();
        }
    }

    @SuppressWarnings("unchecked")
    void addActivityDelegate(ActivityDelegate activityDelegate) {
        delegates.add(activityDelegate);
        activityDelegate.onAttachedToActivity(baseActivity);
    }

    boolean removeActivityDelegate(ActivityDelegate activityDelegate) {
        boolean remove = delegates.remove(activityDelegate);
        if (remove) {
            activityDelegate.onDetachedFromActivity();
        }
        return remove;
    }

    ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate) {
        for (ActivityDelegate delegate : delegates) {
            if (predicate.test(delegate)) {
                return delegate;
            }
        }
        return null;
    }

}
