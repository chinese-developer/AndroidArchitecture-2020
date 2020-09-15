/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.api

import com.android.sdk.net.error.ErrorException

sealed class NetResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : NetResult<T>()
    data class Error(val exception: ErrorException) : NetResult<Nothing>()

    fun whenSuccess(block: (T) -> Unit) {
        if (this is Success<T>) {
            block(this.data)
        }
    }

    fun whenFailure(block: (Error) -> Unit) {
        if (this is Error) {
            if (exception.errCode == ErrorException.TOKEN_EXPIRATION) {

            }
            block(this)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
