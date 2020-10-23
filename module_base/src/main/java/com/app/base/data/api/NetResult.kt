/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.data.api

import com.android.base.utils.ktx.getString
import com.android.sdk.net.error.ErrorException
import com.app.base.AppContext
import com.app.base.R
import com.app.base.router.RouterPath
import com.app.base.toast
import com.blankj.utilcode.util.ActivityUtils

sealed class NetResult<out T : Any> {

    data class Success<out T : Any>(val data: T?) : NetResult<T>()
    data class Error(val exception: ErrorException) : NetResult<Nothing>()

    fun whenSuccess(block: (T?) -> Unit) {
        if (this is Success<T>) {
            block(this.data)
        }
    }

    fun whenFailure(block: (Error) -> Unit) {
        if (this is Error) {
            if (exception.errCode == ErrorException.TOKEN_EXPIRATION) {
                val topActivity = ActivityUtils.getTopActivity()

                if (topActivity != null) {
                    val topActivityName = topActivity::class.java.name
                    if (topActivityName != "com.example.cp.disk.view.activity.LoginAndRegActivity") {
                        toast(getString(R.string.msg_needed_re_login_tips))
                        AppContext.get().appRouter.build(RouterPath.Account.PATH)
                                ./*withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK).*/navigation()
                        AppContext.get().appDataSource.logout()
                        if (topActivityName != "com.example.cp.disk.view.activity.MainActivityTwo") {
                            topActivity.finish()
                        }
                    }
                }
                return
            }
            block(this)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
