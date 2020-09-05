package com.android.sdk.net.service;

import com.android.sdk.net.provider.HttpConfig;

import okhttp3.OkHttpClient;

public class OkHttpRegular {

    private OkHttpClient mOkHttpClient;

    public OkHttpClient getOkHttpClient(HttpConfig httpConfig) {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (httpConfig != null) {
                httpConfig.configHttp(builder);
            }
            mOkHttpClient = builder.build();
        }
        return mOkHttpClient;
    }
}
