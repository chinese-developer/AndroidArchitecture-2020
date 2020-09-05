package com.app.base.app

import com.android.base.utils.android.views.getString
import com.android.sdk.net.NetContext
import com.android.sdk.net.error.ExceptionHandle
import com.android.sdk.net.error.ErrorException
import com.app.base.R
import com.app.base.data.api.HttpResult
import com.app.base.data.api.NetResult

open class BaseRepository {

    suspend fun <T : Any> safeApiCallNoCheckResponse(
            call: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            call()
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    protected suspend fun <T : Any> safeApiCall(
            errorMessage: String? = null,
            call: suspend () -> retrofit2.Response<T>
    ): NetResult<T> {
        return try {
            checkResponse(call(), errorMessage)
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    protected suspend fun <T : Any> safeApiCallWrapResult(
            errorMessage: String? = null,
            call: suspend () -> retrofit2.Response<HttpResult<T>>
    ): NetResult<T> {
        return try {
            checkResponseWrapResult(call(), errorMessage)
        } catch (e: Exception) {
            NetResult.Error(ExceptionHandle.handleException(e))
        }
    }

    private fun <T : Any> checkResponse(
            response: retrofit2.Response<T>,
            errorMessage: String? = null
    ): NetResult<T> {
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                return NetResult.Success(data)
            }
        }

        return if (NetContext.get().connected()) {
            NetResult.Error(ErrorException(response.code(), errorMessage ?: response.message()))
        } else {
            NetResult.Error(ErrorException(response.code(), getString(R.string.error_net_error)))
        }
    }

    private fun <T : Any> checkResponseWrapResult(
            response: retrofit2.Response<HttpResult<T>>,
            errorMessage: String? = null
    ): NetResult<T> {
        val result = response.body()
        if (response.isSuccessful) {
            if (result != null && result.data != null) {
                return NetResult.Success(result.data!!)
            }
        }

        return if (NetContext.get().connected()) {
            NetResult.Error(ErrorException(result?.code ?: response.code(), errorMessage
                    ?: result?.message ?: response.message()))
        } else {
            NetResult.Error(ErrorException(result?.code
                    ?: response.code(), getString(R.string.error_net_error)))
        }
    }
}