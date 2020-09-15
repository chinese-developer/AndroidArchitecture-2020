@file:Suppress("INACCESSIBLE_TYPE")

package com.app.base.data

import android.annotation.SuppressLint
import com.android.base.LogTags
import com.android.sdk.net.https.HttpsUtils
import com.android.sdk.net.provider.ApiHandler
import com.android.sdk.net.provider.ErrorDataAdapter
import com.android.sdk.net.provider.HttpConfig
import com.app.base.data.api.isDataError
import com.app.base.data.api.isLoginExpired
import com.app.base.data.api.newErrorDataStub
import com.app.base.debug.DebugTools
import com.app.base.debug.isOpenDebug
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

internal fun newOkHttpConfig(): HttpConfig {

    return object : HttpConfig {

        private val CONNECTION_TIME_OUT = 10
        private val IO_TIME_OUT = 20

        override fun baseUrl() = DataConfig.baseUrl()

        override fun environment() = DataConfig.environment()

        override fun configRetrofit(
                okHttpClient: OkHttpClient,
                builder: Retrofit.Builder
        ): Boolean = false

        @SuppressLint("BinaryOperationInTimber")
        override fun configHttp(builder: OkHttpClient.Builder) {
            val sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null)
            // 常规配置
            builder.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(IO_TIME_OUT.toLong(), TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager)
                    .hostnameVerifier(HostnameVerifier { hostname, session ->
                        Timber.d("hostnameVerifier called with: hostname 、session = [" + hostname + "、" + session.protocol + "]")
                        true
                    })

            // 调试配置
            if (isOpenDebug()) {
                val httpLoggingInterceptor =
                        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                            override fun log(message: String) {
                                Timber.tag(LogTags.okHttp).i(message)
                            }
                        })
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(httpLoggingInterceptor)
                DebugTools.installStethoHttp(builder)
            }
        }
    }
}


internal fun newErrorDataAdapter(): ErrorDataAdapter = object : ErrorDataAdapter {
    override fun createErrorDataStub(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit,
            value: ResponseBody
    ): Any {
        return newErrorDataStub()
    }

    override fun isErrorDataStub(`object`: Any): Boolean {
        return isDataError(`object`)
    }
}

internal fun newApiHandler(): ApiHandler = ApiHandler { result ->
    // 登录状态已过期，请重新登录、账号在其他设备登陆
    if (isLoginExpired(result.code)) {
        DataConfig.getInstance().publishLoginExpired(result.code)
    }
}