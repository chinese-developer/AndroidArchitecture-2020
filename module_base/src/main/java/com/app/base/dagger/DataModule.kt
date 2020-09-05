package com.app.base.dagger

import android.content.SharedPreferences
import com.android.sdk.net.NetContext
import com.android.sdk.net.gson.GsonUtils
import com.app.base.data.api.*
import com.app.base.data.app.AppRepository
import com.app.base.data.app.StorageManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val OKHTTP_REGULAR = "okhttp_regular"
const val OKHTTP_WITHOUT_TOKEN = "okhttp_without_token"

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonUtils.gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    /**
     * 缓存管理，由 StorageFactory 创建的缓存是不受管理的。
     */
    @Provides
    @Singleton
    fun provideStorageManager(appRepository: AppRepository): StorageManager {
        return appRepository.storageManager
    }

    @Provides
    @Singleton
    fun provideAuthTokenLocalDataSource(
        sharedPreferences: SharedPreferences
    ): AuthTokenLocalDataSource =
        AuthTokenLocalDataSource.getInstance(sharedPreferences)

    @Named(OKHTTP_REGULAR)
    @Provides
    @Singleton
    fun provideOkHttpRegularClient(): OkHttpClient = NetContext.get().httpClient()

    @Named(OKHTTP_WITHOUT_TOKEN)
    @Provides
    @Singleton
    fun provideOkHttpWithoutTokenClient(): OkHttpClient = NetContext.get().httpClientWithoutToken()

    @Provides
    @AuthInterceptorOkHttpClient
    fun provideRegularOkHttpClient(
        @Named(OKHTTP_REGULAR) upstream: OkHttpClient,
        tokenHolder: AuthTokenLocalDataSource
    ): OkHttpClient =
        upstream.newBuilder()
            .addInterceptor(ClientAuthInterceptor(tokenHolder, ""))
            .build()

    @Provides
    @WithoutTokenInterceptorOkHttpClient
    fun provideRegularWithoutTokenOkHttpClient(
        @Named(OKHTTP_WITHOUT_TOKEN) upstream: OkHttpClient
    ): OkHttpClient = upstream.newBuilder().build()

}