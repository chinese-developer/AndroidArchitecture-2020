package com.example.architecture

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.app.base.AppContext
import com.app.base.data.DataConfig
import com.app.base.data.api.ApiParameter
import com.app.base.data.getBaseUrl
import com.example.architecture.home.ui.launch.SplashActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArchAppContext : AppContext() {

    override fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreate() {
        ApiParameter.PLATFORM = getString(R.string.platform_name)
        ApiParameter.BASE_URL = getBaseUrl(getString(R.string.host));
        super.onCreate()
    }

}