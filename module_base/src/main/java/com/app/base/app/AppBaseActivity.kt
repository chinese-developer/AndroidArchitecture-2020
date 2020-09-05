package com.app.base.app

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.android.base.app.activity.BaseActivity
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.android.base.utils.android.compat.StatusBarUtil
import com.app.base.R
import com.app.base.config.AppSettings
import com.app.base.router.RouterManager

abstract class AppBaseActivity : BaseActivity(), AutoDisposeLifecycleOwnerEx {

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)

        if (enableStatusBarLightMode()) {
            AppSettings.setSupportStatusBarLightMode(setStatusBarLightMode())
        }

        if (tintStatusBar()) {
            /** 针对于某些SDK19的机型做的单独配置 */
            StatusBarUtil.setTranslucentSystemUi(this, true, false)
            if (enableStatusBarLightMode() && AppSettings.supportStatusBarLightMode()) {
                StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white))
            } else {
                StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }
        }

        RouterManager.inject(this)
    }

    protected open fun tintStatusBar() = true
    protected open fun enableStatusBarLightMode() = true

}