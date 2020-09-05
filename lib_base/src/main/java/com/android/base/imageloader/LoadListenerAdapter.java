package com.android.base.imageloader;

public abstract class LoadListenerAdapter<T> implements LoadListener<T> {

    @Override
    public void onLoadSuccess(T resource) {
    }

    @Override
    public void onLoadFail() {
    }

    @Override
    public void onLoadStart() {
    }
}
