package com.android.base.app.ui;

import java.util.List;

/**
 * 列表视图行为
 *
 */
public interface RefreshListLayout<T> extends RefreshStateLayout {

    void replaceData(List<T> data);

    void addData(List<T> data);

    PageNumber getPager();

    boolean isEmpty();

    void loadMoreCompleted(boolean hasMore);

    void loadMoreFailed();

    boolean isLoadingMore();

    @Override
    boolean isRefreshing();

}
