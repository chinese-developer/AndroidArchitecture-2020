package com.app.base.scope

import android.view.View
import com.android.sdk.net.NetConfig
import com.drake.statelayout.StateLayout
import kotlinx.coroutines.CancellationException

/**
 * 缺省页作用域
 */
class StateCoroutineScope(val state: StateLayout) : NetCoroutineScope() {

    init {
        state.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isReadCache = !state.loaded
        state.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed) {
            state.showContent()
        }
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        if (!isCacheSucceed) state.showError(e)
    }

    override fun handleError(e: Throwable) {
        NetConfig.onStateError(e, state)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            state.showContent()
        }
        state.trigger()
    }

}
