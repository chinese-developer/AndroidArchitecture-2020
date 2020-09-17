package com.app.base.data

import com.app.base.data.api.ApiParameter
import com.app.base.data.api.ApiParameter.PLATFORM_B
import com.app.base.data.api.ApiParameter.PLATFORM_REGULAR

private const val CATEGORY_HOST = "KEY_FOR_HOST"
private const val H5_HOST = "KEY_FOR_H5_HOST"

internal const val BUILD_DEV = 1
internal const val BUILD_UAT = 2
internal const val BUILD_RELEASE = 3

internal fun cacheCurrentBaseUrl(hostPosition: Int) {
    val list = EnvironmentContext.allCategory()[CATEGORY_HOST]
    if (!list.isNullOrEmpty()) {
        EnvironmentContext.cacheCurrentBaseUrl(CATEGORY_HOST, list[hostPosition].url)
    }
}

fun getBaseUrl(baseUrl: String?): String {
    val preBaseUrl = EnvironmentContext.getPreBaseUrl(CATEGORY_HOST)
    return if (preBaseUrl.isNullOrBlank()) {
        val url = if (baseUrl.isNullOrBlank()) {
            ApiParameter.BASE_URL
        } else {
            baseUrl
        }
        EnvironmentContext.cacheCurrentBaseUrl(CATEGORY_HOST, url)
        url
    } else preBaseUrl
}

internal fun getBaseWebUrl(): String {
    return EnvironmentContext.selected(H5_HOST)?.url ?: ApiParameter.BASE_URL_FOR_H5
}

internal fun firstTimeOpenApp(): Int {
    return when (ApiParameter.PLATFORM) {
        PLATFORM_REGULAR -> {
            val uatUrl = "https://uat.com"
            if (ApiParameter.BASE_URL == uatUrl) BUILD_UAT else BUILD_RELEASE
        }
        PLATFORM_B -> {
            val uatUrl = "https://b.uat.com"
            val releaseUrl = "https://b.release.com"
            when (ApiParameter.BASE_URL) {
                uatUrl -> BUILD_UAT
                releaseUrl -> BUILD_RELEASE
                else -> BUILD_DEV
            }
        }
        else -> BUILD_RELEASE
    }
}

internal fun addAllHost() {
    when (ApiParameter.PLATFORM) {
        PLATFORM_REGULAR -> {
            val uatUrl = "https://uat.com"
            val releaseUrl = "https://release.com"
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", uatUrl))
                add(CATEGORY_HOST, Environment("正式", releaseUrl))
            }
            ApiParameter.PLATFORM_COUNT = 2
        }
        PLATFORM_B -> {
            val uatUrl = "https://b.uat.com"
            val releaseUrl = "https://b.release.com"
            val devUrl = "https://b.dev.com"
            EnvironmentContext.startEdit {
                add(CATEGORY_HOST, Environment("预演", uatUrl))
                add(CATEGORY_HOST, Environment("正式", releaseUrl))
                add(CATEGORY_HOST, Environment("开发", devUrl))
            }
            ApiParameter.PLATFORM_COUNT = 3
        }
    }
}

internal fun addReleaseHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("正式", ApiParameter.BASE_URL))
        add(H5_HOST, Environment("正式", ApiParameter.BASE_URL))
    }
}