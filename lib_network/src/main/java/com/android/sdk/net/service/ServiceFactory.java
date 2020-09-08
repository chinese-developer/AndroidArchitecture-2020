package com.android.sdk.net.service;

import com.android.sdk.net.gson.ErrorJsonLenientConverterFactory;
import com.android.sdk.net.gson.GsonUtils;
import com.android.sdk.net.progress.RequestProgressInterceptor;
import com.android.sdk.net.progress.ResponseProgressInterceptor;
import com.android.sdk.net.progress.UrlProgressListener;
import com.android.sdk.net.provider.HttpConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ServiceFactory {

    private final OkHttpClient mOkHttpClient;
    private final String mBaseUrl;
    private final Retrofit mRetrofit;

    ServiceFactory(OkHttpClient okHttpClient, HttpConfig httpConfig) {
        mOkHttpClient = okHttpClient;
        mBaseUrl = httpConfig.baseUrl();
        String environment = httpConfig.environment();

        Timber.tag("===OKHTTP===").i("当前环境是: " + environment + "\nBaseUrl: " + mBaseUrl + "\nOkHttp: " + mOkHttpClient);

        Retrofit.Builder builder = new Retrofit.Builder();

        if (!httpConfig.configRetrofit(mOkHttpClient, builder)) {
            builder.baseUrl(mBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(new ErrorJsonLenientConverterFactory(GsonConverterFactory.create(GsonUtils.INSTANCE.gson())));
        }

        mRetrofit = builder.build();
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public <T> T createWithUploadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new RequestProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public <T> T createWithDownloadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new ResponseProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public String baseUrl() {
        return mBaseUrl;
    }

}
