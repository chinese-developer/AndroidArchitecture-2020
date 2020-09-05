package com.android.base.app.ui;

import com.android.base.adapter.DataManager;

public class AutoPageNumber extends PageNumber {

    private final DataManager dataManager;
    private final RefreshListLayout refreshListLayout;

    public AutoPageNumber(RefreshListLayout refreshListLayout, DataManager dataManager) {
        this.refreshListLayout = refreshListLayout;
        this.dataManager = dataManager;
    }

    @Override
    public int getCurrentPage() {
        return calcPageNumber(dataManager.getDataSize());
    }

    @Override
    public int getLoadingPage() {
        if (refreshListLayout.isRefreshing()) {
            return getPageStart();
        } else {
            return getCurrentPage() + 1;
        }
    }

    @Override
    public int getItemCount() {
        return dataManager.getDataSize();
    }

}
