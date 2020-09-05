package com.example.architecture

import android.content.Intent
import com.app.base.AppContext
import com.app.base.data.DataConfig
import com.example.architecture.home.SplashActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArchAppContext : AppContext() {

    override fun restartApp() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreate() {
        /** 数据层在 dagger 注入前初始化，保证 [DataModule] 需要的依赖都已经生成 */
        DataConfig.init(this)

        super.onCreate()
    }

}