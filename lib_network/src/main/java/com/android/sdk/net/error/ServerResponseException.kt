package com.android.sdk.net.error

/**
 * 500 503 服务器异常错误
 */
class ServerResponseException(val code: Int, msg: String?) : ErrorException(code, msg)