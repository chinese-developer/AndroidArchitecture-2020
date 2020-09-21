package com.app.base.app

import android.os.Bundle
import com.android.base.app.activity.BaseActivity
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.android.base.utils.android.compat.immersive
import com.android.base.utils.android.compat.immersiveDark
import com.app.base.router.RouterManager

abstract class AppBaseActivity : BaseActivity(), AutoDisposeLifecycleOwnerEx {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (tintStatusBar()) {
            if (enableStatusBarLightMode()) {
                immersive()
            } else {
                immersiveDark()
            }
        }

        RouterManager.inject(this)
    }

    protected open fun tintStatusBar() = true
    protected open fun enableStatusBarLightMode() = false

}