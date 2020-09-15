package com.android.sdk.net.error

open class ErrorException(val errCode: Int, errMessage: String?) : Exception(errMessage) {

    companion object {
        const val ERROR_UNAUTHORIZED = 401
        const val ERROR_FORBIDDEN = 403
        const val ERROR_NOT_FOUND = 404
        const val ERROR_REQUEST_TIMEOUT = 408
        const val ERROR_INTERNAL_SERVER_ERROR = 500
        const val ERROR_SERVICE_UNAVAILABLE = 503

        /**
         * 未知错误
         */
        const val ERROR_UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val ERROR_PARSE_ERROR = 1001

        /**
         * 网络错误
         */
        const val ERROR_NETWORK_ERROR = 1002

        /**
         * 主机地址错误
         */
        const val ERROR_UNKNOWN_HOST_ERROR = 1003

        /**
         * 证书出错
         */
        const val ERROR_SSL_ERROR = 1005

        /**
         * 连接超时
         */
        const val ERROR_TIMEOUT_ERROR = 1006

        /**
         * token 过期
         */
        const val TOKEN_EXPIRATION = 9999
    }
}