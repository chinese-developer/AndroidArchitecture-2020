package com.android.base.imageloader;

public interface ProgressListener {

    void onProgress(String url, ProgressInfo progressInfo);

    default void onError(long id, String url, Throwable throwable){}

}
