package com.app.base.widget.verticalviewpager.transforms;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class StackTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setTranslationX(page.getWidth() * -position);
        page.setTranslationY(position < 0 ? position * page.getHeight() : 0f);
    }
}
