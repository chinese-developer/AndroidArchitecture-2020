package com.app.base.app

import com.android.base.app.fragment.BaseFragment
import com.android.base.rx.AutoDisposeLifecycleOwnerEx
import com.app.base.router.AppRouter
import javax.inject.Inject

/**
 * fragment with AutoDisposeLifecycleOwner and injects
 */
open class AppBaseFragment : BaseFragment(), AutoDisposeLifecycleOwnerEx {

    @Inject protected lateinit var appRouter: AppRouter
    @Inject protected lateinit var errorHandler: ErrorHandler

}