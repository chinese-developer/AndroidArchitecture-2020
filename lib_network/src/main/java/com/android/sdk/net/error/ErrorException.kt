package com.android.sdk.net.error

open class ErrorException(val errCode: Int, errMessage: String?) : Exception(errMessage)