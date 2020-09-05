package com.android.base.app.fragment;

import android.os.Bundle;

import com.android.base.adapter.DataManager;
import com.android.base.app.ui.AutoPageNumber;
import com.android.base.app.ui.PageNumber;
import com.android.base.app.ui.RefreshListLayout;
import com.ztiany.loadmore.adapter.ILoadMore;
import com.ztiany.loadmore.adapter.OnLoadMoreListener;
import com.ztiany.loadmore.adapter.WrapperAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用的RecyclerView列表界面：支持下拉刷新和加载更多。
 *
 * @param <T> 当前列表使用的数据类型
 */
public abstract class BaseListFragment<T> extends BaseStateFragment implements RefreshListLayout<T> {

    /**
     * 加载更多
     */
    private ILoadMore loadMore;

    /**
     * 列表数据管理
     */
    private DataManager<T> dataManager;

    /**
     * 分页页码
     */
    private PageNumber pageNumber;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (dataManager == null) {
            throw new NullPointerException("you need set DataManager");
        }
    }

    protected final void setDataManager(@NonNull DataManager<T> dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Default PageNumber is {@link AutoPageNumber}
     *
     * @param recyclerAdapter adapter
     * @return recycler adapter wrapper
     */
    protected final RecyclerView.Adapter setupLoadMore(@NonNull RecyclerView.Adapter<?> recyclerAdapter) {
        if (dataManager == null) {
            throw new IllegalStateException("you should setup a DataManager before call this method");
        }
        return setupLoadMore(recyclerAdapter, new AutoPageNumber(this, dataManager));
    }

    protected final RecyclerView.Adapter setupLoadMore(@NonNull RecyclerView.Adapter<?> recyclerAdapter, @NonNull PageNumber pageNumber) {
        this.pageNumber = pageNumber;

        WrapperAdapter wrap = WrapperAdapter.wrap(recyclerAdapter);
        loadMore = wrap;
        loadMore.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                BaseListFragment.this.onLoadMore();
            }

            @Override
            public boolean canLoadMore() {
                return !isRefreshing();
            }
        });
        return wrap;
    }

    @Override
    protected void onRefresh() {
        onStartLoad();
    }

    protected void onLoadMore() {
        onStartLoad();
    }

    @Override
    public final boolean canRefresh() {
        return !isLoadingMore();
    }

    /**
     * call by {@link #onRefresh()} or {@link #onLoadMore()}, you can get current isLoading type from {@link #isRefreshing()} or {@link #isLoadingMore()}.
     */
    protected void onStartLoad() {
    }

    @Override
    public void replaceData(List<T> data) {
        dataManager.replaceAll(data);
    }

    @Override
    public void addData(List<T> data) {
        dataManager.addItems(data);
    }

    protected final ILoadMore getLoadMoreController() {
        return loadMore;
    }

    @Override
    public PageNumber getPager() {
        return pageNumber;
    }

    @Override
    public boolean isEmpty() {
        return dataManager.isEmpty();
    }

    @Override
    public boolean isLoadingMore() {
        return loadMore != null && loadMore.isLoadingMore();
    }

    @Override
    public void loadMoreCompleted(boolean hasMore) {
        if (loadMore != null) {
            loadMore.loadCompleted(hasMore);
        }
    }

    @Override
    public void loadMoreFailed() {
        if (loadMore != null) {
            loadMore.loadFail();
        }
    }

}