/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.app

import android.content.SharedPreferences
import androidx.core.content.edit
import com.android.cache.Storage
import com.android.cache.getEntity
import com.app.base.data.models.LoggedInUser
import javax.inject.Inject

class LoginLocalDataSource @Inject constructor(private val prefs: SharedPreferences) {

    var userStorage: Storage? = null

    var user: LoggedInUser
        get() {
            val token = prefs.getString(KEY_USER_TOKEN, null)
            val username = prefs.getString(KEY_USER_NAME, null)
            val userAvatar = prefs.getString(KEY_USER_AVATAR, null)
            val netEaseUid = prefs.getString(KEY_USER_NETEASE_UID, null)
            if (token == null || username == null) {
                return LoggedInUser.NOT_LOGIN
            }
            return LoggedInUser(
                token = token,
                displayName = username,
                avatarUrl = userAvatar,
                netEaseUid = netEaseUid
            )
        }
        set(value) {
            prefs.edit {
                putString(KEY_USER_TOKEN, value.token)
                putString(KEY_USER_NAME, value.displayName)
                putString(KEY_USER_AVATAR, value.avatarUrl)
                putString(KEY_USER_NETEASE_UID, value.netEaseUid)
            }
        }

    fun logout() {
        prefs.edit {
            putString(KEY_USER_TOKEN, null)
            putString(KEY_USER_NAME, null)
            putString(KEY_USER_AVATAR, null)
            putString(KEY_USER_NETEASE_UID, null)
        }
    }

    companion object {
        private const val KEY_USER_TOKEN = "KEY_USER_TOKEN"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_USER_AVATAR = "KEY_USER_AVATAR"
        const val KEY_USER_NETEASE_UID = "KEY_USER_NETEASE_DATA"
    }
}