package com.app.base.scope

import android.view.View
import com.android.sdk.net.NetConfig
import com.drake.brv.PageRefreshLayout
import kotlinx.coroutines.CancellationException

@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
class PageCoroutineScope(val page: PageRefreshLayout) : NetCoroutineScope() {

    val index get() = page.index

    init {
        page.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun start() {
        isReadCache = !page.loaded
        page.trigger()
    }

    override fun readCache(succeed: Boolean) {
        if (succeed && !animate) {
            page.showContent()
        }
        page.loaded = succeed
    }

    override fun catch(e: Throwable) {
        super.catch(e)
        page.showError(e)
    }

    override fun finally(e: Throwable?) {
        super.finally(e)
        if (e == null || e is CancellationException) {
            page.showContent()
        }
        page.trigger()
    }

    override fun handleError(e: Throwable) {
        if (page.loaded || !page.stateEnabled) {
            NetConfig.onError(e)
        } else {
            NetConfig.onStateError(e, page)
        }
    }
}