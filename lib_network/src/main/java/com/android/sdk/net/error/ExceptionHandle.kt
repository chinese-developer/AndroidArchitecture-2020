package com.android.sdk.net.error

import android.app.Application
import android.net.ParseException
import com.android.sdk.net.R
import com.android.sdk.net.error.ErrorException.Companion.ERROR_FORBIDDEN
import com.android.sdk.net.error.ErrorException.Companion.ERROR_INTERNAL_SERVER_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_NETWORK_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_NOT_FOUND
import com.android.sdk.net.error.ErrorException.Companion.ERROR_PARSE_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_REQUEST_TIMEOUT
import com.android.sdk.net.error.ErrorException.Companion.ERROR_SERVICE_UNAVAILABLE
import com.android.sdk.net.error.ErrorException.Companion.ERROR_SSL_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_TIMEOUT_ERROR
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNAUTHORIZED
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNKNOWN
import com.android.sdk.net.error.ErrorException.Companion.ERROR_UNKNOWN_HOST_ERROR
import com.android.sdk.net.error.ErrorException.Companion.TOKEN_EXPIRATION
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ExceptionHandle {

    @JvmStatic
    fun handleException(app: Application, e: Throwable): ErrorException {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    ERROR_UNAUTHORIZED -> RequestParamsException(e.code(), app.getString(R.string.operation_is_not_authorized))
                    ERROR_FORBIDDEN -> RequestParamsException(e.code(), app.getString(R.string.request_denied))
                    ERROR_NOT_FOUND -> RequestParamsException(e.code(), app.getString(R.string.resource_does_not_exist))
                    ERROR_REQUEST_TIMEOUT -> RequestParamsException(e.code(), app.getString(R.string.server_execution_timeout))
                    ERROR_INTERNAL_SERVER_ERROR -> ServerResponseException(e.code(), app.getString(R.string.server_error))
                    ERROR_SERVICE_UNAVAILABLE -> ServerResponseException(e.code(), app.getString(R.string.server_unavailable))
                    TOKEN_EXPIRATION -> ServerResponseException(e.code(), app.getString(R.string.login_expired_please_log_in_again))
                    else -> ErrorException(e.code(), app.getString(R.string.network_error))
                }
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ErrorException(ERROR_PARSE_ERROR, app.getString(R.string.parsing_error))
            }

            is ConnectException -> {
                ErrorException(ERROR_NETWORK_ERROR, app.getString(R.string.something_wrong_with_the_network))
            }

            is SSLException -> {
                ErrorException(ERROR_SSL_ERROR, app.getString(R.string.certificate_verification_failed))
            }

            is SocketTimeoutException -> {
                ErrorException(ERROR_TIMEOUT_ERROR, app.getString(R.string.connection_timed_out))
            }

            is UnknownHostException -> {
                ErrorException(ERROR_UNKNOWN_HOST_ERROR, app.getString(R.string.something_wrong_with_the_network))
            }

            else -> ErrorException(ERROR_UNKNOWN, app.getString(R.string.unknown_error))

        }
    }
}