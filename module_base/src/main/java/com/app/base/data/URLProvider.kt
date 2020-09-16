package com.app.base.data

import com.app.base.data.api.ApiParameter

private const val CATEGORY_HOST = "KEY_FOR_HOST"
private const val H5_HOST = "KEY_FOR_H5_HOST"

internal const val BUILD_DEV = 1
internal const val BUILD_UAT = 2
internal const val BUILD_RELEASE = 3

internal const val PLATFORM_WX = "wx"
internal const val PLATFORM_WF = "wf"
internal const val PLATFORM_CFT = "cft"
internal const val PLATFORM_ZS = "zs"

internal fun select(hostPosition: Int) {
    val list = EnvironmentContext.allCategory()[CATEGORY_HOST]
    if (!list.isNullOrEmpty()) {
        EnvironmentContext.select(CATEGORY_HOST, list[hostPosition])
    }
}

fun getBaseUrl(baseUrl: String?): String {
    val lastUrl = EnvironmentContext.lastUrl(CATEGORY_HOST)
    return if (lastUrl.isNullOrBlank()) if (baseUrl.isNullOrBlank()) ApiParameter.BASE_URL else baseUrl else lastUrl
}

internal fun getEnvironment(): String {
    return EnvironmentContext.selected(CATEGORY_HOST)?.name ?: ApiParameter.PLATFORM
}

internal fun getBaseWebUrl(): String {
    return EnvironmentContext.selected(H5_HOST)?.url ?: ApiParameter.BASE_URL_FOR_H5
}

internal fun addAllHost(): Int {
    return when (ApiParameter.PLATFORM) {
        PLATFORM_WX -> {
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", "https://m.wxpre.com"))
                add(CATEGORY_HOST, Environment("正式", "https://wx1125.com"))
            }
            ApiParameter.PLATFORM_COUNT = 2
            if (ApiParameter.BASE_URL == "https://m.wxpre.com") BUILD_UAT else BUILD_RELEASE
        }
        PLATFORM_WF -> {
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", "https://m.wfpre.com"))
                add(CATEGORY_HOST, Environment("正式", "https://wf11.APP"))
            }
            ApiParameter.PLATFORM_COUNT = 2
            if (ApiParameter.BASE_URL == "https://m.wfpre.com") BUILD_UAT else BUILD_RELEASE
        }
        PLATFORM_CFT -> {
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", "https://m.cflotterypre.com"))
                add(CATEGORY_HOST, Environment("正式", "https://cf994.com"))
            }
            ApiParameter.PLATFORM_COUNT = 2
            if (ApiParameter.BASE_URL == "https://m.cflotterypre.com") BUILD_UAT else BUILD_RELEASE
        }
        PLATFORM_ZS -> {
            val uatUrl = "https://m.ustest01.com"
            val releaseUrl = "http://18.163.112.59:98"
            val devUrl = "https://zs061.com"
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", uatUrl))
                add(CATEGORY_HOST, Environment("开发", devUrl))
                add(CATEGORY_HOST, Environment("正式", releaseUrl))
            }
            ApiParameter.PLATFORM_COUNT = 3
            when (ApiParameter.BASE_URL) {
                uatUrl-> BUILD_UAT
                releaseUrl -> BUILD_RELEASE
                else -> BUILD_DEV
            }
        }
        else -> BUILD_RELEASE
    }
}

internal fun addReleaseHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("正式", ApiParameter.BASE_URL))
        add(H5_HOST, Environment("正式", ApiParameter.BASE_URL))
    }
}