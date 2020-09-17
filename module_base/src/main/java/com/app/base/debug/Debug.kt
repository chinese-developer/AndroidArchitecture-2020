@file:JvmName("Debug")

package com.app.base.debug

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.app.base.BuildConfig

fun isOpenDebug(): Boolean {
    return BuildConfig.DEBUG
}

fun ifOpenDebug(action: () -> Unit) {
    if (isOpenDebug()) {
        action()
    }
}

/** 开启严苛模式 */
fun startStrictMode() {
    StrictMode.setVmPolicy(
        VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
    )
    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeathOnNetwork()
            .build()
    )
}