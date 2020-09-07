package com.android.sdk.net

import com.android.sdk.net.provider.ApiHandler
import com.android.sdk.net.provider.ErrorDataAdapter
import com.android.sdk.net.provider.HttpConfig

interface NetProvider {
    fun aipHandler(): ApiHandler?

    fun httpConfig(): HttpConfig?

    fun errorDataAdapter(): ErrorDataAdapter
}

class NetProviderImpl : NetProvider {

    var mApiHandler: ApiHandler? = null
    var mHttpConfig: HttpConfig? = null
    var mErrorDataAdapter: ErrorDataAdapter? = null

    override fun aipHandler(): ApiHandler? {
        return mApiHandler
    }

    override fun httpConfig(): HttpConfig? {
        return mHttpConfig
    }

    override fun errorDataAdapter(): ErrorDataAdapter {
        return mErrorDataAdapter!!
    }

    fun checkRequired() {
        if (mErrorDataAdapter == null || mHttpConfig == null) {
            throw NullPointerException("You must provide following object：ErrorMessage, mErrorDataAdapter, mNetworkChecker, HttpConfig.")
        }
    }

}