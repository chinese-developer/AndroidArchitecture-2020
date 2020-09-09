package com.app.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import org.jetbrains.annotations.NotNull;

/**
 * 一个 "紧贴联动" 的 Behavior
 */
public class FollowBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "FollowBehavior";

    NestedScrollView mDependency;

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull View child, @NotNull View dependency) {
//        boolean isValidDependency = dependency instanceof NestedScrollView;
//        if(isValidDependency)
//            mDependency = (NestedScrollView)dependency;
        return true;
    }

    @Override
    public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, View child, View dependency) {
        int height = child.getHeight();
        child.setTop(dependency.getTop() - height);
        child.setBottom(dependency.getTop());
        return true;
    }
}
