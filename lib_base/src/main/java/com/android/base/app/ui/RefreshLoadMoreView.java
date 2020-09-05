package com.android.base.app.ui;

/**
 * 对下拉刷新的抽象
 */
public interface RefreshLoadMoreView {

    void autoRefresh();

    void refreshCompleted();

    void loadMoreCompleted(boolean hasMore);

    void loadMoreFailed();

    void setRefreshHandler(RefreshHandler refreshHandler);

    void setLoadMoreHandler(LoadMoreHandler loadMoreHandler);

    boolean isRefreshing();

    boolean isLoadingMore();

    void setRefreshEnable(boolean enable);

    void setLoadMoreEnable(boolean enable);

    interface RefreshHandler {
        void onRefresh();
    }

    interface LoadMoreHandler {
        void onRefresh();
    }

}