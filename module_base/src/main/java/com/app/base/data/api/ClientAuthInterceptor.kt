/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ClientAuthInterceptor(
    private val authTokenDataSource: AuthTokenLocalDataSource,
    private val clientId: String
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        if (!authTokenDataSource.authToken.isNullOrEmpty()) {
            requestBuilder
                .addHeader(
                    "Authorization",
                    "Bearer ${authTokenDataSource.authToken}"
                )
                .addHeader("Content-type", "application/json")
        } else {
            val url = chain.request().url.newBuilder()
                .addQueryParameter("client_id", clientId).build()
            requestBuilder.url(url)
        }
        return chain.proceed(requestBuilder.build())
    }
}