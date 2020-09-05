package com.android.sdk.net.provider;

import androidx.annotation.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public interface HttpConfig {

    /**
     * base url for build retrofit.
     */
    String baseUrl();

    /**
     * environment
     *
     * check URLProvider.kt
     * @return
     */
    String environment();

    /**
     * config OkHttp client.
     */
    void configHttp(@NonNull OkHttpClient.Builder builder);

    /**
     * default config is {@link retrofit2.converter.gson.GsonConverterFactory}、{@link retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory} with {@link Schedulers#io()}
     *
     * @return if true, default config  do nothing, and all config up to you.
     */
    boolean configRetrofit(@NonNull OkHttpClient okHttpClient, @NonNull Retrofit.Builder builder);

}
