package com.app.base.widget.verticalviewpager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;


public class VerticalVPOnTouchListener implements View.OnTouchListener {
    private static final String TAG = "VerticalViewPager";
    private VerticalViewPager verticalViewPager;//the container ViewPager
    private float lastMotionY = Float.MIN_VALUE;
    private float downY = Float.MIN_VALUE;

    /**
     * @param verticalViewPager the container
     */
    public VerticalVPOnTouchListener(VerticalViewPager verticalViewPager) {
        this.verticalViewPager = verticalViewPager;
    }

    /**
     * 1.dispatch ACTION_DOWN,ACTION_UP,ACTION_CANCEL to child<br>
     * 2.hack ACTION_MOVE
     *
     * @param v
     * @param e
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        Log.i(TAG, "onTouchEvent " + ", action " + e.getAction() + ", e.rawY " + e.getRawY() + ",lastMotionY " + lastMotionY + ",downY " + downY);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (downY == Float.MIN_VALUE && lastMotionY == Float.MIN_VALUE) {
                    //not start from MOTION_DOWN, the child dispatch this first motion
                    downY = e.getRawY();
                    break;
                }

                float diff = e.getRawY() - (lastMotionY == Float.MIN_VALUE ? downY : lastMotionY);
                lastMotionY = e.getRawY();
                diff = diff / 2; //slow down viewpager scroll
                Log.e(TAG, "scrollX " + verticalViewPager.getScrollX() + ",basescrollX " + verticalViewPager.getBaseScrollX());

                if (verticalViewPager.getScrollX() != verticalViewPager.getBaseScrollX()) {
                    if (fakeDragVp(v, e, diff)) return true;
                } else {
                    if (ViewCompat.canScrollVertically(v, (-diff) > 0 ? 1 : -1)) {
                        Log.e(TAG, "scroll vertically  " + diff + ", move.lastMotionY " + e.getY());
                        break;
                    } else {
                        verticalViewPager.beginFakeDrag();
                        fakeDragVp(v, e, diff);
                        adjustDownMotion(v, e);
                        return true;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (verticalViewPager.isFakeDragging()) {
                    try {
                        verticalViewPager.endFakeDrag();
                    } catch (Exception e1) {
                        Log.e(TAG, "", e1);
                    }
                }
                reset();
                break;
        }

        return false;
    }

    private boolean fakeDragVp(View v, MotionEvent e, float diff) {
        if (verticalViewPager.isFakeDragging()) {
            float step = diff;
            int expScrollX = (int) (verticalViewPager.getScrollX() - step);
            if (isDiffSign(expScrollX - verticalViewPager.getBaseScrollX(), verticalViewPager.getScrollX() - verticalViewPager.getBaseScrollX())) {
                step = verticalViewPager.getScrollX() - verticalViewPager.getBaseScrollX();
            }
            verticalViewPager.fakeDragBy(step);
            Log.e(TAG, "fake drag, diff " + diff + ",step " + step + ",scrollX " + verticalViewPager.getScrollX());
            adjustDownMotion(v, e);

            return true;
        }
        return false;
    }

    /**
     * fake a ACTION_DOWN to adjust child's motion track
     *
     * @param v
     * @param e
     */
    private void adjustDownMotion(View v, MotionEvent e) {
        MotionEvent fakeDownEvent = MotionEvent.obtain(e);
        fakeDownEvent.setAction(MotionEvent.ACTION_DOWN);
        v.dispatchTouchEvent(fakeDownEvent);
    }

    private boolean isDiffSign(float a, float b) {
        return Math.abs(a + b) < Math.abs(a - b);
    }


    public void reset() {
        downY = Float.MIN_VALUE;
        lastMotionY = Float.MIN_VALUE;
    }
}
