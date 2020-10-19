package com.example.architecture.home.dagger

import com.android.base.utils.android.ActFragWrapper.create
import com.app.base.dagger.OKHTTP_REGULAR
import com.app.base.dagger.OKHTTP_WITHOUT_TOKEN
import com.app.base.data.api.ApiParameter
import com.app.base.data.api.AuthInterceptorOkHttpClient
import com.app.base.data.api.DeEnvelopingConverter
import com.app.base.data.api.WithoutTokenInterceptorOkHttpClient
import com.example.architecture.home.HomeApiService
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object HomeModule {

    @Provides
    @Singleton
    @Named(OKHTTP_REGULAR)
    fun provideHomeApiService(
        @AuthInterceptorOkHttpClient client: Lazy<OkHttpClient>,
        gson: Gson
    ): HomeApiService {
        return Retrofit.Builder()
            .baseUrl(ApiParameter.BASE_URL)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return client.get().newCall(request)
                }
            })
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
//            .addConverterFactory(DeEnvelopingConverter(gson))
//            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(OKHTTP_WITHOUT_TOKEN)
    fun provideHomeApiServiceWithoutToken(
        @WithoutTokenInterceptorOkHttpClient client: Lazy<OkHttpClient>,
        gson: Gson
    ): HomeApiService {
        return Retrofit.Builder()
            .baseUrl(ApiParameter.BASE_URL)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return client.get().newCall(request)
                }
            })
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build()
            .create(HomeApiService::class.java)
    }
}