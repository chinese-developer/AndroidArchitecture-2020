package com.android.base.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.base.adapter.recycler.MultiTypeAdapter;
import com.android.base.app.fragment.BaseListFragment;
import com.android.base.app.ui.AutoPageNumber;

public class TestListFragment extends BaseListFragment<String> {

    @Override
    protected void onViewPrepared(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewPrepared(view, savedInstanceState);
        MultiTypeAdapter recyclerAdapter = new MultiTypeAdapter(getContext());
        setupLoadMore(recyclerAdapter, new AutoPageNumber(this,recyclerAdapter));
    }

}
