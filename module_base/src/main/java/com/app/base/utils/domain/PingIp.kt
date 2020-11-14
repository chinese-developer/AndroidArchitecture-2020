package com.app.base.utils.domain

import java.io.Serializable

data class PingIp(
    var ip: String,
    var pingCount: Int,
    var pingTime: String? = null, // ms
    var pingTimeOut: Int,
    var resultBuffer: StringBuffer,
    var result: Boolean = false
): Serializable