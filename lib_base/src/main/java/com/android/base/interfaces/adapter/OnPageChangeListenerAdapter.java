package com.android.base.interfaces.adapter;

import androidx.viewpager.widget.ViewPager;

public interface OnPageChangeListenerAdapter extends ViewPager.OnPageChangeListener {

    @Override
    default void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    default void onPageSelected(int position) {
    }

    @Override
    default void onPageScrollStateChanged(int state) {
    }

}
