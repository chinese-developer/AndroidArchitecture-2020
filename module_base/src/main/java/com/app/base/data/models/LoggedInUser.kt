/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.models

import android.os.Parcelable
import com.android.base.utils.android.views.getString
import com.app.base.AppContext
import com.app.base.R
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@JsonClass(generateAdapter = true)
data class LoggedInUser(
    @SerializedName("token")
    val token: String?,

    @SerializedName("username")
    val displayName: String?,

    @SerializedName("header")
    val avatarUrl: String?,

    @SerializedName("w_uid")
    val netEaseUid: String?
) : Serializable, Parcelable {

    fun loginButtonDesc(): String {
        return if (AppContext.appDataSource().isLoggedIn()) {
            getString(R.string.logout)
        } else {
            getString(R.string.login)
        }
    }

    fun showSyncButton(): Boolean = netEaseUid == null || netEaseUid == "0"

    companion object {
        val NOT_LOGIN = LoggedInUser(null, null, null, null)
    }
}