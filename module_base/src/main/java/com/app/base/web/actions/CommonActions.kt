package com.app.base.web.actions

import com.android.base.utils.android.compat.getStatusBarHeight
import com.app.base.AppContext
import com.app.base.data.api.ApiParameter
import com.app.base.router.RouterPath
import com.app.base.router.RouterPath.Main.HOME
import com.app.base.web.BaseWebFragment
import com.app.base.web.ResultReceiver
import timber.log.Timber

private const val GET_VERSION_METHOD = "getVersion"//获取版本号
private const val SIGN_METHOD = "sign"//签名加密
private const val GET_TOKEN_METHOD = "getToken"//获得登录token
private const val GO_BACK_METHOD = "goback"//退出web
private const val SHOW_HEADER_METHOD = "showHeader"//展示和隐藏头部
private const val GET_STATUS_HEIGHT = "getStatusBarHeight"//获取状态栏高度

internal fun doAction(
    method: String,
    args: Array<String>?,
    resultReceiver: ResultReceiver?,
    fragment: BaseWebFragment
) {
    when (method) {
        GET_VERSION_METHOD -> getVersion(resultReceiver)
        SIGN_METHOD -> sign(args, resultReceiver)
        GET_TOKEN_METHOD -> getToken(resultReceiver)
        SHOW_HEADER_METHOD -> showHeader(args, fragment)
        GO_BACK_METHOD -> fragment.exit()
        GET_STATUS_HEIGHT -> getStatusBarHeight(fragment, resultReceiver)
    }
}

fun jumpToHomePage() {
    AppContext.appRouter().build(RouterPath.Main.PATH)
        .withInt(RouterPath.Main.PAGE_KEY, HOME)
        .navigation()
}

private fun getStatusBarHeight(
    fragment: androidx.fragment.app.Fragment,
    resultReceiver: ResultReceiver?
) {
    fragment.context?.let {
        resultReceiver?.result(fragment.requireContext().getStatusBarHeight().toString())
    }
}

private fun showHeader(args: Array<String>?, fragment: BaseWebFragment) {
    if (args.isNullOrEmpty()) {
        return
    }
    val showHeader = args[0].toBoolean()
    fragment.setHeaderVisible(showHeader)
}

private fun getToken(resultReceiver: ResultReceiver?) {
    val appToken = AppContext.appDataSource().appToken()
    Timber.d("js call GetToken, return $appToken")
    resultReceiver?.result(appToken)
}

private fun getVersion(resultReceiver: ResultReceiver?) {
    val version = ApiParameter.API_VERSION_VALUE
    Timber.d("js call GetVersion, return $version")
    resultReceiver?.result(version)
}

private fun sign(args: Array<String>?, resultReceiver: ResultReceiver?) {
    try {
        val sign = if (args.isNullOrEmpty()) {
            ApiParameter.genSignAndGenerateRequestParams("", false, false, false)
        } else {
            ApiParameter.genSignAndGenerateRequestParams(
                args[0],
                args[1].toBoolean(),
                args[2].toBoolean(),
                args[3].toBoolean()
            )
        }
        Timber.d("js call Sign, return $sign")
        resultReceiver?.result(sign)
    } catch (e: Exception) {
        Timber.e(e, "js call Sign error")
    }
}




