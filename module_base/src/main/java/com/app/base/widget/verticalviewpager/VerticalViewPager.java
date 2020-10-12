package com.app.base.widget.verticalviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.app.base.widget.verticalviewpager.transforms.DefaultTransformer;

import java.io.Serializable;

/**
 * Don't intercept any MotionEvent, delegate to {@link VerticalVPOnTouchListener}<br>
 */
public class VerticalViewPager extends ViewPager implements Serializable {

    private static final String TAG = "DummyViewPager";
    private int baseScrollX;
    private int currentScrollState;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new DefaultTransformer());// vertical scroll trick
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    baseScrollX = getScrollX();
                }
                currentScrollState = state;
            }
        });

    }


    public int getBaseScrollX() {
        return baseScrollX;
    }

    public void setBaseScrollX(int baseScrollX) {
        this.baseScrollX = baseScrollX;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (currentScrollState == ViewPager.SCROLL_STATE_IDLE) {
            baseScrollX = getScrollX();
        }
    }
}
