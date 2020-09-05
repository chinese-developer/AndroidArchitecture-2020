package com.example.architecture.home

import android.app.Activity
import android.content.Intent
import com.android.base.app.activity.ActivityDelegate
import com.app.base.router.RouterPath
import com.app.base.widget.dialog.TipsManager
import com.app.base.widget.dialog.showConfirmDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivityResultCallback(private val activity: MainActivity) :
    ActivityDelegate<MainActivity> {

    init {
        activity.addDelegate(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == RouterPath.Account.REQUEST_CODE && data != null) {
            when (data.getIntExtra(RouterPath.Account.LOGIN_TYPE, 0)) {
                RouterPath.Account.LOGIN_BY_REGISTER -> {
                    // 新用户注册，逻辑处理
                    showConfirmDialog(activity) {
                        messageId = R.string.tips
                        positiveId = R.string.confirm
                        positiveColor = R.color.green_main
                        titleId = R.string.title
                        positiveListener = {
                            TipsManager.showMessage("appRouter.build().navigation()")
                        }
                    }
                }

                RouterPath.Account.LOGIN_BY_OTHER -> {
                    // 老用户登录，逻辑处理

                }
            }
        }
    }

}