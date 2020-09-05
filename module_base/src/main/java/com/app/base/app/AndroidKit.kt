package com.app.base.app

import android.annotation.SuppressLint
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidKit @Inject constructor() {

    @SuppressLint("MissingPermission") fun isConnected() = NetworkUtils.isConnected()

    fun getAppVersionName(): String = AppUtils.getAppVersionName()

}