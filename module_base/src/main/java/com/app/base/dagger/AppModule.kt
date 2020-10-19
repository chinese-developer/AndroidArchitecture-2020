package com.app.base.dagger

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import androidx.room.Room
import com.app.base.AppContext
import com.app.base.data.api.ApiParameter
import com.app.base.data.api.AuthInterceptorOkHttpClient
import com.app.base.data.api.DeEnvelopingConverter
import com.app.base.data.app.AppApiService
import com.app.base.data.app.AppDao
import com.app.base.data.app.AppDataBase
import com.app.base.utils.ConnectivityChecker
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideAppDao(database: AppDataBase): AppDao {
        return database.appDao()
    }

    @Provides
    @Singleton
    fun provideAppApiService(
        @AuthInterceptorOkHttpClient client: Lazy<OkHttpClient>,
        gson: Gson
    ): AppApiService {
        return Retrofit.Builder()
            .baseUrl(ApiParameter.BASE_URL)
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call {
                    return client.get().newCall(request)
                }
            })
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(AppApiService::class.java)
    }

    @Provides
    @Singleton
    fun connectivityChecker(): ConnectivityChecker? {
        val connectivityManager = AppContext.get().getSystemService<ConnectivityManager>()
        return if (connectivityManager != null) {
            ConnectivityChecker(connectivityManager)
        } else {
            null
        }
    }

}

