package com.android.sdk.net.error

/**
 *  对应网络请求后台定义的错误信息
 * @param msg 网络请求错误信息
 * @param code 网络请求错误码
 * @param tag 应对错误码判断为错时但是后端又返回了需要使用的数据(建议后端修改). 一般在Convert中设置数据
 */
class ResponseException(val code: Int, val msg: String) : ErrorException(code, msg)
