package com.android.base.widget;

import android.content.res.TypedArray;
import android.view.View;

import com.android.base.app.ui.StateLayoutConfig;

import androidx.annotation.NonNull;

public abstract class StateProcessor {

    protected abstract void onInitialize(SimpleMultiStateLayout simpleMultiStateView);

    protected abstract void onParseAttrs(TypedArray typedArray);

    protected abstract void processStateInflated(@StateLayoutConfig.ViewState int viewState, @NonNull View view);

    protected abstract StateLayoutConfig getStateLayoutConfigImpl();

}
