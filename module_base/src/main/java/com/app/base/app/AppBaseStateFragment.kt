package com.app.base.app

import com.android.base.app.fragment.BaseStateFragment
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.app.base.router.AppRouter
import javax.inject.Inject

open class AppBaseStateFragment : BaseStateFragment(), AutoDisposeLifecycleOwnerEx {

    @Inject protected lateinit var appRouter: AppRouter
    @Inject protected lateinit var errorHandler: ErrorHandler

}