package com.app.base.data.api

import com.android.sdk.net.core.Response
import com.android.sdk.net.error.ServerResponseException
import com.google.gson.annotations.SerializedName

data class HttpResult<T> @JvmOverloads constructor(
        @SerializedName("code")
        private val status: Int,
        @SerializedName("msg")
        private val message: String? = null,
        @SerializedName("data")
        private val data: T? = null)
    : Response<T> {

    override fun getData(): T? = data
    override fun getCode(): Int = status
    override fun getMessage(): String? = message
    override fun isSuccessful(): Boolean = status == SUCCESS_STATUS

    override fun toString(): String {
        return "Result{ code=$status, data=$data, message=$message }"
    }
}

/*返回成功*/
const val SUCCESS_STATUS: Int = 200
/*Json解析错误*/
const val DATA_ERROR_STATUS: Int = -8088
/*登录状态已过期，请重新登录*/
const val LOGIN_EXPIRED_STATUS: Int = 1
/*账号在其他设备登陆*/
const val LOGIN_EXPIRED_SSO_STATUS: Int = 11
/*没有状态码异常*/
const val ERROR_STATUS: Int = -1
/*未知异常*/
const val UNKNOWN_ERROR_STATUS: Int = -999

fun isLoginExpired(code: Int): Boolean = code == LOGIN_EXPIRED_STATUS
fun isSSOLoginExpired(code: Int): Boolean = code == LOGIN_EXPIRED_SSO_STATUS
fun isDataError(data: Any): Boolean {
    if (data is HttpResult<*>) {
        return data == DATA_ERROR_STATUS
    }
    return false
}

fun newErrorDataStub(): HttpResult<Nothing> = HttpResult(DATA_ERROR_STATUS)

/** `true` data non-null [HttpResult.data]. */
fun HttpResult<*>.isNonNull(): Boolean = data != null

/** `true` 登录过期、token过期、或 该账号在其他设备登录了 */
fun Throwable.isAuthError(): Boolean {
    return this is ServerResponseException && (isLoginExpired(this.code) || isSSOLoginExpired(this.code))
}