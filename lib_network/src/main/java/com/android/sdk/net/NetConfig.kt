package com.android.sdk.net

import android.app.Application
import android.view.View
import android.widget.Toast
import com.android.sdk.net.error.ExceptionHandle
import com.android.sdk.net.error.RequestParamsException
import com.android.sdk.net.error.ResponseException
import com.android.sdk.net.error.ServerResponseException
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
    lateinit var downloadOrUploadServiceWithoutToken: ServiceFactory
        private set
    lateinit var apiHandler: ApiHandler
        private set

    var onError: Throwable.() -> Unit = {
        val error = ExceptionHandle.handleException(this)
        printStackTrace()
        Toast.makeText(app, error.message ?: "未知错误", Toast.LENGTH_SHORT).show()
    }

    var onStateError: Throwable.(view: View) -> Unit = {
        when (this) {
            is ServerResponseException,
            is RequestParamsException,
            is ResponseException,
            is NullPointerException -> onError(this)
            else -> printStackTrace()
        }
    }

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
        downloadOrUploadServiceWithoutToken = okHttpWithoutTokenFactory.getServiceFactory(httpConfig)
    }
}