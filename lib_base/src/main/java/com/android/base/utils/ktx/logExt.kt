package com.android.base.utils.ktx

import com.android.base.TagsFactory
import timber.log.Timber

fun logDebug(message: String) {
    Timber.tag(TagsFactory.debug).d(message)
}

fun logError(message: String) {
    Timber.tag(TagsFactory.error).e(message)
}

fun logThread(message: String) {
    Timber.tag(TagsFactory.debug).d("[%s] %s", Thread.currentThread().name, message)
}
