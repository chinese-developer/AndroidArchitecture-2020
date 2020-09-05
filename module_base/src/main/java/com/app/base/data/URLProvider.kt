package com.app.base.data

import com.app.base.data.api.ApiParameter

private const val CATEGORY_HOST = "开发环境"
private const val H5_HOST = "H5环境"

internal const val BUILD_RELEASE = 999
internal const val BUILD_TEST = 1
internal const val BUILD_STRESS = 2
internal const val BUILD_DEV = 3

internal fun getBaseUrl(): String {
    return EnvironmentContext.selected(CATEGORY_HOST).url
}

internal fun getEnvironment(): String {
    return EnvironmentContext.selected(CATEGORY_HOST).name
}

internal fun getBaseWebUrl(): String {
    return EnvironmentContext.selected(H5_HOST).url
}

internal fun addAllHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("测试", ApiParameter.BASE_URL))
        add(CATEGORY_HOST, Environment("开发", ApiParameter.BASE_URL))
        add(CATEGORY_HOST, Environment("正式", ApiParameter.BASE_URL))

        add(H5_HOST, Environment("测试", ApiParameter.BASE_URL))
        add(H5_HOST, Environment("开发", ApiParameter.BASE_URL))
        add(H5_HOST, Environment("正式", ApiParameter.BASE_URL))
    }
}

internal fun addReleaseHost() {
    EnvironmentContext.startEdit {
        add(CATEGORY_HOST, Environment("正式", ApiParameter.BASE_URL))
        add(H5_HOST, Environment("正式", ApiParameter.BASE_URL))
    }
}