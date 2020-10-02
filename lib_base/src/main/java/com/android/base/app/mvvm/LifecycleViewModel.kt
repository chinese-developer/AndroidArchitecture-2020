package com.android.base.app.mvvm

import androidx.lifecycle.*

open class LifecycleViewModel : ViewModel(), LifecycleEventObserver, LifecycleOwner {

    private var mLifecycleRegistry: LifecycleRegistry? = null

    override fun getLifecycle(): LifecycleRegistry {

        if (mLifecycleRegistry == null) {
            mLifecycleRegistry = LifecycleRegistry(this)
        }
        return mLifecycleRegistry as LifecycleRegistry
    }

    open fun needObserver(): Boolean {
        return true
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        // 不需要生命周期 直接返回
        if (!needObserver()) return
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            else -> onAny()
        }
        lifecycle.handleLifecycleEvent(event)
    }

    open fun onCreate() {}
    open fun onStart() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}
    open fun onDestroy() {}
    open fun onAny() {}
}