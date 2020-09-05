/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.base.utils.ktx

import timber.log.Timber

inline fun tryCatchAll(message: String = "", action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        Timber.e("Failed to $message. ${e.message}")
        e.printStackTrace()
    }
}