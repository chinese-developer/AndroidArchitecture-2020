package com.app.base.router

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppInfo(
        val rule_id: String,
        val rule_type: Int,
        val soft_icon: String? = null,
        val soft_name: String? = null,
        val type_name: String? = null
) : Parcelable