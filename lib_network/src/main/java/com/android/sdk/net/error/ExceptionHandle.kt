package com.android.sdk.net.error

import android.net.ParseException
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
                    UNAUTHORIZED -> RequestParamsException(e.code(), "操作未授权")
                    FORBIDDEN -> RequestParamsException(e.code(), "请求被拒绝")
                    NOT_FOUND -> RequestParamsException(e.code(), "资源不存在")
                    REQUEST_TIMEOUT -> RequestParamsException(e.code(), "服务器执行超时")
                    INTERNAL_SERVER_ERROR -> ServerResponseException(e.code(), "服务器内部错误")
                    SERVICE_UNAVAILABLE -> ServerResponseException(e.code(), "服务器不可用")
                    else -> ErrorException(e.code(), "网络错误")
                }
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ErrorException(PARSE_ERROR, "解析错误")
            }

            is ConnectException -> {
                ErrorException(NETWORK_ERROR, "连接失败")
            }

            is SSLException -> {
                ErrorException(SSL_ERROR, "证书验证失败")
            }

            is SocketTimeoutException -> {
                ErrorException(TIMEOUT_ERROR, "连接超时")
            }

            is UnknownHostException -> {
                ErrorException(UNKNOWN_HOST_ERROR, "主机地址未知")
            }

            else -> ErrorException(UNKNOWN, "未知错误")

        }
    }

    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val SERVICE_UNAVAILABLE = 503

    /**
     * 未知错误
     */
    private const val UNKNOWN = 1000

    /**
     * 解析错误
     */
    private const val PARSE_ERROR = 1001

    /**
     * 网络错误
     */
    private const val NETWORK_ERROR = 1002

    /**
     * 主机地址错误
     */
    private const val UNKNOWN_HOST_ERROR = 1003

    /**
     * 证书出错
     */
    private const val SSL_ERROR = 1005

    /**
     * 连接超时
     */
    private const val TIMEOUT_ERROR = 1006


}