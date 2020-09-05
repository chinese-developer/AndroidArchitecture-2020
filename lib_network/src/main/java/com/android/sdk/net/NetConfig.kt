package com.android.sdk.net

import android.app.Application
import com.android.sdk.net.provider.ApiHandler
import com.android.sdk.net.provider.ErrorDataAdapter
import com.android.sdk.net.provider.HttpConfig
import com.android.sdk.net.service.OkHttpRegular
import com.android.sdk.net.service.OkHttpWithoutToken
import com.android.sdk.net.service.ServiceFactory
import okhttp3.OkHttpClient

object NetConfig {

    lateinit var app: Application
        private set
    lateinit var netProvider: NetProvider
        private set
    lateinit var oKHttpRegular: OkHttpClient
        private set
    lateinit var okHttpWithoutToken: OkHttpClient
        private set
    lateinit var retrofitWithoutToken: ServiceFactory
        private set
    lateinit var apiHandler: ApiHandler
        private set

    fun initNet(
        app: Application,
        apiHandler: ApiHandler,
        httpConfig: HttpConfig,
        errorDataAdapter: ErrorDataAdapter
    ) {
        this.app = app
        this.apiHandler = apiHandler
        val netProvider = NetProviderImpl()
        val okHttpWithoutTokenFactory = OkHttpWithoutToken()
        netProvider.mHttpConfig = httpConfig
        netProvider.mErrorDataAdapter = errorDataAdapter
        oKHttpRegular = OkHttpRegular().getOkHttpClient(httpConfig)
        okHttpWithoutToken = okHttpWithoutTokenFactory.getOkHttpClient(httpConfig)
        retrofitWithoutToken = okHttpWithoutTokenFactory.getServiceFactory(httpConfig)
    }
}