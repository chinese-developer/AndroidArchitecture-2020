package com.app.base.app

import android.annotation.SuppressLint
import com.android.base.utils.ktx.getString
import com.android.sdk.net.error.ErrorException
import com.android.sdk.net.error.ExceptionHandle
import com.app.base.R
import com.app.base.data.api.HttpResult
import com.app.base.data.api.NetResult
import com.blankj.utilcode.util.NetworkUtils

open class BaseRepository {

    suspend fun <T : Any> safeApiCallNoNeedCheckCode(
        call: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    protected suspend fun <T : Any> safeApiCallWithoutCode(
        errorMessage: String? = null,
        call: suspend () -> retrofit2.Response<T>
    ): NetResult<T> {
        return try {
            checkResponse(call(), errorMessage)
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    protected suspend fun <T : Any> safeApiCall(
        errorMessage: String? = null,
        call: suspend () -> retrofit2.Response<HttpResult<T>>
    ): NetResult<T> {
        return try {
            checkResponseCode(call(), errorMessage)
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    @SuppressLint("MissingPermission")
    private fun <T : Any> checkResponse(
        response: retrofit2.Response<T>,
        errorMessage: String? = null
    ): NetResult<T> {
        if (response.isSuccessful) {
            return NetResult.Success(response.body())
        }

        return if (NetworkUtils.isConnected()) {
            NetResult.Error(ErrorException(response.code(), errorMessage ?: response.message()))
        } else {
            NetResult.Error(ErrorException(response.code(), getString(R.string.msg_error_net_error)))
        }
    }

    @SuppressLint("MissingPermission")
    private fun <T : Any> checkResponseCode(
        response: retrofit2.Response<HttpResult<T>>,
        errorMessage: String? = null
    ): NetResult<T> {
        val result = response.body()
        if (response.isSuccessful && result?.code == 200) {
            return NetResult.Success(result.data)
        }

        return if (NetworkUtils.isConnected()) {
            NetResult.Error(
                ErrorException(
                    result?.code ?: response.code(), errorMessage
                        ?: result?.message ?: response.message()
                )
            )
        } else {
            NetResult.Error(
                ErrorException(
                    result?.code
                        ?: response.code(), getString(R.string.msg_error_net_error)
                )
            )
        }
    }
}