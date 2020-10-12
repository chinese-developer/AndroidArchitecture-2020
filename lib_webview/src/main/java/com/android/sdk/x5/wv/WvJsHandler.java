package com.android.sdk.x5.wv;

public interface WvJsHandler<T,R> {
    void handler(T data, ResponseCallback<R> callback);
}
