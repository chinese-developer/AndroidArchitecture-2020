package com.android.sdk.net.error

import android.net.ParseException
import com.android.sdk.net.error.ErrorException.Companion.ERROR_FORBIDDEN
import com.android.sdk.net.error.ErrorException.Companion.ERROR_INTERNAL_SERVER_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_NETWORK_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_NOT_FOUND
import com.android.sdk.net.error.ErrorException.Companion.ERROR_PARSE_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_REQUEST_TIMEOUT
import com.android.sdk.net.error.ErrorException.Companion.ERROR_SERVICE_UNAVAILABLE
import com.android.sdk.net.error.ErrorException.Companion.ERROR_SSL_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_TIMEOUT_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNAUTHORIZED
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNKNOWN
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNKNOWN_HOST_ERROR
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ExceptionHandle {

    fun handleException(e: Throwable): ErrorException {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    ERROR_UNAUTHORIZED -> RequestParamsException(e.code(), "操作未授权")
                    ERROR_FORBIDDEN -> RequestParamsException(e.code(), "请求被拒绝")
                    ERROR_NOT_FOUND -> RequestParamsException(e.code(), "资源不存在")
                    ERROR_REQUEST_TIMEOUT -> RequestParamsException(e.code(), "服务器执行超时")
                    ERROR_INTERNAL_SERVER_ERROR -> ServerResponseException(e.code(), "服务器内部错误")
                    ERROR_SERVICE_UNAVAILABLE -> ServerResponseException(e.code(), "服务器不可用")
                    else -> ErrorException(e.code(), "网络错误")
                }
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ErrorException(ERROR_PARSE_ERROR, "解析错误")
            }

            is ConnectException -> {
                ErrorException(ERROR_NETWORK_ERROR, "连接失败")
            }

            is SSLException -> {
                ErrorException(ERROR_SSL_ERROR, "证书验证失败")
            }

            is SocketTimeoutException -> {
                ErrorException(ERROR_TIMEOUT_ERROR, "连接超时")
            }

            is UnknownHostException -> {
                ErrorException(ERROR_UNKNOWN_HOST_ERROR, "主机地址未知")
            }

            else -> ErrorException(ERROR_UNKNOWN, "未知错误")

        }
    }
}