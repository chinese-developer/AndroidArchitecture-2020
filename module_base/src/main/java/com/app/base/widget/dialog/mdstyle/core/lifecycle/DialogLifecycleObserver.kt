/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

internal class DialogLifecycleObserver(private val dismiss: () -> Unit) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = dismiss

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() = dismiss
}