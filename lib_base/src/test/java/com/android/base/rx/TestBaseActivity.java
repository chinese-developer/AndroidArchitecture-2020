package com.android.base.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.app.activity.BaseActivity;

public class TestBaseActivity extends BaseActivity {

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
    }

    @Override
    protected Object layout() {
        return 0;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
    }

}