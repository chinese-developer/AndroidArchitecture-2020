package com.android.base.interfaces.adapter;

import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

public interface DrawerListenerAdapter extends DrawerLayout.DrawerListener {

    @Override
    default void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    default void onDrawerOpened(View drawerView) {
    }

    @Override
    default void onDrawerClosed(View drawerView) {
    }

    @Override
    default void onDrawerStateChanged(int newState) {
    }

}
