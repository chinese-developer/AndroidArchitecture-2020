package com.android.sdk.net.error

/**
 * 400..499 请求参数错误
 */
class RequestParamsException(val code: Int, msg: String?) : ErrorException(code, msg)