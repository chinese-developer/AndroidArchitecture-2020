package com.android.base.rx;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.base.app.fragment.deprecated.BaseStateFragmentJava;

public class TestStateFragmentJava extends BaseStateFragmentJava {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewPrepared(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewPrepared(view, savedInstanceState);
        getStateLayoutConfig();
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
    }

    @Override
    public void refreshCompleted() {
        super.refreshCompleted();
    }

}
