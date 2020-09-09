/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.ui.launch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.permission.AutoPermissionRequester
import com.android.base.permission.Permission
import com.app.base.AppContext
import com.app.base.R
import com.app.base.router.RouterManager
import com.app.base.router.RouterPath
import timber.log.Timber

@Route(path = RouterPath.Launcher.PATH)
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RouterManager.inject(this)

        setContentView(R.layout.activity_launcher)

        // 后台返回时可能启动这个页面 http://blog.csdn.net/jianiuqi/article/details/54091181
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }

        requestMustPermission()

    }

    private fun requestMustPermission() {
        AutoPermissionRequester.with(this)
            .permission(
                Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE
            )
            .onDenied {
                Timber.d("requestMustPermission onDenied")
                supportFinishAfterTransition()
            }
            .onGranted {
                Timber.d("requestMustPermission onGranted")
                window.decorView.post {
                    AppContext.appRouter().build(RouterPath.Main.PATH).navigation()
                    finish()
//            overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out)
//            supportFinishAfterTransition()
                }
            }.request()
    }
}