package com.android.base.app.ui;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

class SwipeRefreshView implements RefreshView {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RefreshHandler refreshHandler;

    SwipeRefreshView(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * SwipeRefreshLayout
     */
    @Override
    public void autoRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        doRefresh();
    }

    @Override
    public void refreshCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshHandler(final RefreshHandler refreshHandler) {
        this.refreshHandler = refreshHandler;
        swipeRefreshLayout.setOnRefreshListener(this::doRefresh);
    }

    private void doRefresh() {
        if (refreshHandler.canRefresh()) {
            refreshHandler.onRefresh();
        } else {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }
    }

    @Override
    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }
}
