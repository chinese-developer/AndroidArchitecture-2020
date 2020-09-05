package com.android.sdk.net;

import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.ErrorDataAdapter;
import com.android.sdk.net.provider.HttpConfig;
import com.android.sdk.net.provider.NetworkChecker;
import com.android.sdk.net.service.OkHttpRegular;
import com.android.sdk.net.service.ServiceFactory;
import com.android.sdk.net.service.OkHttpWithoutToken;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

public class NetContext {

    private static volatile NetContext CONTEXT;

    public static NetContext get() {
        if (CONTEXT == null) {
            synchronized (NetContext.class) {
                if (CONTEXT == null) {
                    CONTEXT = new NetContext();
                }
            }
        }
        return CONTEXT;
    }

    private NetContext() {
        oKHttpRegular = new OkHttpRegular();
        okHttpWithoutToken = new OkHttpWithoutToken();
    }

    private NetProvider mNetProvider;
    private OkHttpRegular oKHttpRegular;
    private OkHttpWithoutToken okHttpWithoutToken;

    public Builder newBuilder() {
        return new Builder();
    }

    private void init(@NonNull NetProvider netProvider) {
        mNetProvider = netProvider;
    }

    public boolean connected() {
        return mNetProvider.isConnected();
    }

    public ApiHandler aipHandler() {
        return mNetProvider.aipHandler();
    }

    public NetProvider netProvider() {
        NetProvider retProvider = mNetProvider;

        if (retProvider == null) {
            throw new RuntimeException("NetContext has not be initialized");
        }
        return retProvider;
    }

    public OkHttpClient httpClient() {
        return oKHttpRegular.getOkHttpClient(netProvider().httpConfig());
    }

    public OkHttpClient httpClientWithoutToken() {
        return okHttpWithoutToken.getOkHttpClient(netProvider().httpConfig());
    }

    public ServiceFactory retrofitWithoutToken() {
        return okHttpWithoutToken.getServiceFactory(netProvider().httpConfig());
    }

    public static class Builder {

        private NetProviderImpl mNetProvider = new NetProviderImpl();

        public Builder aipHandler(@NonNull ApiHandler apiHandler) {
            mNetProvider.mApiHandler = apiHandler;
            return this;
        }

        public Builder httpConfig(@NonNull HttpConfig httpConfig) {
            mNetProvider.mHttpConfig = httpConfig;
            return this;
        }

        public Builder errorDataAdapter(@NonNull ErrorDataAdapter errorDataAdapter) {
            mNetProvider.mErrorDataAdapter = errorDataAdapter;
            return this;
        }

        public Builder networkChecker(@NonNull NetworkChecker networkChecker) {
            mNetProvider.mNetworkChecker = networkChecker;
            return this;
        }

        public void setup() {
            mNetProvider.checkRequired();
            NetContext.get().init(mNetProvider);
        }
    }

}
