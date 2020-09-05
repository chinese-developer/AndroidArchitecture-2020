package com.android.base.app.ui;

import android.view.View;

/**
 * RefreshLoadMoreView Factory
 *
 */
public class RefreshLoadViewFactory {

    private static Factory sFactory;

    public static RefreshLoadMoreView createRefreshView(View view) {
        if (sFactory != null) {
            return sFactory.createRefreshView(view);
        }
        throw new IllegalArgumentException("RefreshLoadViewFactory does not support create RefreshLoadMoreView . the view ：" + view);
    }

    public static void registerFactory(Factory factory) {
        sFactory = factory;
    }

    public interface Factory {
        RefreshLoadMoreView createRefreshView(View view);
    }

}
