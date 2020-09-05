package com.android.base.app.ui;

/**
 * 对下拉刷新的抽象
 */
public interface RefreshView {

    void autoRefresh();

    void refreshCompleted();

    void setRefreshHandler(RefreshHandler refreshHandler);

    boolean isRefreshing();

    void setRefreshEnable(boolean enable);

    abstract class RefreshHandler {

        public boolean canRefresh() {
            return true;
        }

        public abstract void onRefresh();

    }

}