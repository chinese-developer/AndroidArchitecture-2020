package com.android.base.app.ui;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * RefreshView Factory
 *
 */
public class RefreshViewFactory {

    private static Factory sFactory;

    public static RefreshView createRefreshView(View view) {

        if (sFactory != null) {
            return sFactory.createRefreshView(view);
        }
        if (view instanceof SwipeRefreshLayout) {
            return new SwipeRefreshView((SwipeRefreshLayout) view);
        }
        throw new IllegalArgumentException("RefreshViewFactory does not support create RefreshView . the view ：" + view);
    }

    public static void registerFactory(Factory factory) {
        sFactory = factory;
    }

    public interface Factory {
        RefreshView createRefreshView(View view);
    }
}
