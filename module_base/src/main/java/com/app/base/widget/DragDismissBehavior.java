package com.app.base.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import org.jetbrains.annotations.NotNull;

/**
 * 一个 "下拉回退" 的 Behavior
 */
public class DragDismissBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "DragDismissBehavior";

    int mDragToDismissRange = 200;  // 下拉范围

    private Context mContext;
    private CoordinatorLayout mParent;
    private View mNestedScrollingChild;
    private int mOriginTop;

    public DragDismissBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onLayoutChild(@NotNull CoordinatorLayout parent, @NotNull View child, int layoutDirection) {
        if(mViewDragHelper == null)
            mViewDragHelper = ViewDragHelper.create(parent, TOUCH_SLOP_SENSITIVITY, mDragCallback);

        if(mParent == null)
            mParent = parent;

        if(mNestedScrollingChild == null)
            mNestedScrollingChild = child;

        mOriginTop = mNestedScrollingChild.getTop();
        return false;
    }

    // ------ 滑动部分 begin ------
    private void smoothScrollTo(int finalTop){
        View child = mNestedScrollingChild;
        mViewDragHelper.smoothSlideViewTo(child, 0, finalTop);
        ViewCompat.postOnAnimation(child, new SettleRunnable(child));
    }

    /**
     * smoothScrollTo() 中用到 mScroller,
     * 以此 SettleRunnable 代替 @Override computeScroll().
     *
     */
    private class SettleRunnable implements Runnable {

        private final View mView;

        SettleRunnable(View view) {
            mView = view;
        }

        @Override
        public void run() {
            if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this);    // 递归调用
            }
        }
    }

    private void scrollTo(int finalTop){
        View child = mNestedScrollingChild;
        child.setTop(finalTop);
        ViewCompat.postInvalidateOnAnimation(child);
    }
    // ------ 滑动部分 end ------

    // ------ 事件分发 begin ------
    float preY, curY, dy;

    @Override
    public boolean onInterceptTouchEvent(@NotNull CoordinatorLayout parent, @NotNull View child, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                preY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                curY = event.getY();
                dy = curY - preY;
                preY = curY;
                break;
        }

        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(@NotNull CoordinatorLayout parent, @NotNull View child, @NotNull MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);

        return true;
    }

    private boolean isScrollDown() {
        return dy > 0;
    }

    private boolean canScrollDown(View child) {
        return !ViewCompat.canScrollVertically(child, -1);
    }

    private boolean canScrollUp(View child) {
        return ViewCompat.canScrollVertically(child, 1);
    }

    private ViewDragHelper mViewDragHelper;
    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;
    private DragCallback mDragCallback = new DragCallback();

    private class DragCallback extends ViewDragHelper.Callback{
        // 仅捕获 mNestedScrollingChild
        @Override
        public boolean tryCaptureView(@NotNull View child, int pointerId) {
            boolean captureView = false;

            if(child == mNestedScrollingChild) {
                if(!canScrollUp(child) && canScrollDown(child))    // 无法滑动, 直接捕获
                    captureView = true;

                if (isScrollDown() && canScrollDown(child))        // 向下滑且滑不动, 则捕获 child
                    captureView = true;
            }

            return captureView;
        }

        // 控制边界, 防止 mNestedScrollingChild 的头部超出边界
        @Override
        public int clampViewPositionVertical(@NotNull View child, int top, int dy) {
            if(child == mNestedScrollingChild){
                int newTop = top;
                newTop = Math.max(newTop, mOriginTop - mDragToDismissRange);
                newTop = Math.min(newTop, mOriginTop + mDragToDismissRange);
                return newTop;
            }
            return top;
        }

        // 需要指定 "滑动范围", 否则不会捕获 mNestedScrollingChild
        @Override
        public int getViewVerticalDragRange(@NotNull View child) {
            return mDragToDismissRange;
        }

        // 手指释放的时候回调
        @Override
        public void onViewReleased(@NotNull View releasedChild, float xvel, float yvel) {
            resetTopOrDismiss(releasedChild);   // 释放时, 看滑动距离, 决定是否 dismiss
        }
    }

    /**
     * 释放手指时, 判断 top 决定 "复位" 或 "dismiss"
     */
    private void resetTopOrDismiss(View releasedChild){
        if(releasedChild != mNestedScrollingChild)
            return ;

        int curTop = releasedChild.getTop();

        int sign = curTop - mOriginTop > 0? 1: -1;
        if(Math.abs(curTop - mOriginTop) > mDragToDismissRange * 0.9){
            smoothScrollTo(mOriginTop + mDragToDismissRange * sign);
            // dissmiss
            mOnDismissListener.onDismiss();

        }else
            smoothScrollTo(mOriginTop);
    }
    // ------ 事件分发 end ------

    // ------ NestScroll begin ------
    // 嵌套滑动响应, 若 mNestedScrollingChild 未实现 NestedScrollingChild 接口,
    // 将无法响应这部分的行为, 交互上稍显不连贯。
    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child, @NotNull View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child, @NotNull View target, int dx, int dy, int[] consumed) {
        if(target != mNestedScrollingChild)
            return ;

        int curTop = child.getTop();
        int newTop = curTop - dy;

        if (dy < 0) { // Downward
            if (canScrollDown(target)) {   // mNestedScrollingChild 内部无法下滑时, 捕获它, 滑动其本身
                if (newTop - mOriginTop <= mDragToDismissRange) {
                    consumed[1] = dy;
                }else
                    consumed[1] = curTop - mOriginTop - mDragToDismissRange;  // 余下的一小段距离

                    ViewCompat.offsetTopAndBottom(child, -consumed[1]);
            }
        }else{ // Upward
            if (curTop > mOriginTop && canScrollDown(target)) {   // mNestedScrollingChild 内部无法下滑时, 捕获它, 滑动其本身
                if (newTop - mOriginTop >= 0) {
                    consumed[1] = dy;
                }else{
                    consumed[1] = curTop - mOriginTop;  // 余下的一小段距离
                }

                ViewCompat.offsetTopAndBottom(child, -consumed[1]);
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child, @NotNull View target) {
        if(target != mNestedScrollingChild)
            return ;

        resetTopOrDismiss(child);   // 释放时, 看滑动距离, 决定是否 dismiss
    }
    // ------ NestScroll end ------

    // ------ 对外接口 begin ------
    public interface OnDismissListener{
        void onDismiss();
    }

    private OnDismissListener mOnDismissListener = new FinishActivity();

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    private class FinishActivity implements OnDismissListener{
        @Override
        public void onDismiss() {
            if(mContext instanceof Activity){
                ((Activity)mContext).finishAfterTransition();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static DragDismissBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();

        if (!(behavior instanceof DragDismissBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with DragDismissBehavior");
        }
        return (DragDismissBehavior) behavior;
    }
    // ------ 对外接口 end ------
}
