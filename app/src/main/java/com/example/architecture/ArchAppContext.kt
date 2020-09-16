package com.example.architecture

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.app.base.AppContext
import com.app.base.data.DataConfig
import com.example.architecture.home.ui.launch.SplashActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArchAppContext : AppContext() {

    override fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}