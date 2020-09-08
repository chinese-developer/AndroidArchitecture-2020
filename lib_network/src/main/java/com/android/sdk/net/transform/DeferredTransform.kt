package com.android.sdk.net.transform

import kotlinx.coroutines.Deferred


fun <T, R> Deferred<T>.transform(block: (T) -> R): DeferredTransform<T, R> {
    return DeferredTransform(this, block)
}

data class DeferredTransform<T, R>(
    val deferred: Deferred<T>,
    val block: (T) -> R
)