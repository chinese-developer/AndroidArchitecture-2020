package com.android.sdk.net;

import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.ErrorDataAdapter;
import com.android.sdk.net.provider.HttpConfig;
import com.android.sdk.net.provider.NetworkChecker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface NetProvider {

    boolean isConnected();

    @Nullable
    ApiHandler aipHandler();

    @Nullable
    HttpConfig httpConfig();

    @NonNull
    ErrorDataAdapter errorDataAdapter();

}

class NetProviderImpl implements NetProvider {

    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;
    ErrorDataAdapter mErrorDataAdapter;
    NetworkChecker mNetworkChecker;

    @Override
    public boolean isConnected() {
        return mNetworkChecker.isConnected();
    }

    @Nullable
    @Override
    public ApiHandler aipHandler() {
        return mApiHandler;
    }

    @Nullable
    @Override
    public HttpConfig httpConfig() {
        return mHttpConfig;
    }

    @NonNull
    @Override
    public ErrorDataAdapter errorDataAdapter() {
        return mErrorDataAdapter;
    }

    void checkRequired() {
        if (mErrorDataAdapter == null || mNetworkChecker == null || mHttpConfig == null) {
            throw new NullPointerException("You must provide following object：ErrorMessage, mErrorDataAdapter, mNetworkChecker, HttpConfig.");
        }
    }

}