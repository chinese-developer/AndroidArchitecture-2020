package com.android.sdk.net.core

data class WrapResponse<T>(val errorCode: Int, val errorMsg: String, val response: T) :
    Response<T> {

    override fun getCode() = errorCode

    override fun getMessage() = errorMsg

    override fun isSuccessful(): Boolean = errorCode == 0

    override fun getData(): T = response

}