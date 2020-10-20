/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.api

import android.content.SharedPreferences
import androidx.core.content.edit
import com.android.base.utils.ktx.getString
import com.app.base.AppContext
import com.app.base.R
import com.drake.tooltip.toast
import javax.inject.Inject

/**
 * Local storage for auth token.
 */
class AuthTokenLocalDataSource @Inject constructor(private val prefs: SharedPreferences) {

    private var _authToken: String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    var authToken: String? = _authToken
        set(value) {
            prefs.edit { putString(KEY_ACCESS_TOKEN, value) }
            field = value
        }

    fun resetToken() {
        _authToken = prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearData() {
        prefs.edit { KEY_ACCESS_TOKEN to null }
        _authToken = null
        authToken = null
    }

    fun checkIfHasToken(): Boolean {
        if (authToken.isNullOrBlank()) {
            AppContext.get().toast(getString(R.string.error_not_login))
            return false
        }
        return true
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"

        @Volatile
        private var INSTANCE: AuthTokenLocalDataSource? = null

        fun getInstance(
            sharedPreferences: SharedPreferences
        ): AuthTokenLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthTokenLocalDataSource(sharedPreferences).also {
                    INSTANCE = it
                }
            }
        }
    }
}