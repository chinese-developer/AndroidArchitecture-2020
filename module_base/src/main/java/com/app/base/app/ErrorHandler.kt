package com.app.base.app

import com.android.base.app.Sword
import com.app.base.AppContext
import com.app.base.R
import com.app.base.data.api.NetResult
import com.app.base.data.api.isLoginExpired
import com.app.base.data.api.isSSOLoginExpired
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.showConfirmDialog
import com.drake.tooltip.toast
import javax.inject.Inject

interface ErrorHandler {

    /** 直接处理异常，比如根据 [createMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(netResultError: NetResult.Error)

    /** 直接处理异常，自定义消息处理*/
    fun handleError(netResultError: NetResult.Error, processor: ((CharSequence) -> Unit))

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常*/
    fun handleGlobalError(code: Int)

}

class AppErrorHandler @Inject constructor() : ErrorHandler {

    override fun handleError(netResultError: NetResult.Error) {
        handleError(netResultError) {
            AppContext.get().toast(it)
        }
    }

    override fun handleError(netResultError: NetResult.Error, processor: (CharSequence) -> Unit) {
        val code = netResultError.exception.errCode
        if (isLoginExpired(code) || isSSOLoginExpired(code)) {
            if (showReLoginDialog(code)) {
                return
            }
        }
        AppContext.get().toast(netResultError.exception.message ?: "")
    }

    override fun handleGlobalError(code: Int) {
        showReLoginDialog(code)
    }

    private fun showReLoginDialog(code: Int): Boolean {
        val currentActivity = Sword.get().currentActivity ?: return false

        currentActivity.showConfirmDialog {

            messageId = if (isLoginExpired(code)) {
                R.string.login_expired_re_login_tips
            } else {
                R.string.sso_re_login_tips
            }

            noNegative()

            positiveListener = {
                AppContext.appRouter().build(RouterPath.Main.PATH)
                        .withInt(RouterPath.ACTION_KEY, RouterPath.Main.ACTION_RE_LOGIN)
                        .navigation()
            }

        }.setCancelable(false)

        return true
    }

}