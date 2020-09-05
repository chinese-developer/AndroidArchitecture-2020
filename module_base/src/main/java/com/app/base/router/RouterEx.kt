package com.app.base.router

import android.app.Activity
import com.app.base.AppContext

fun Activity.requireLoggedIn(): Boolean {
    if (!AppContext.appDataSource().isLoggedIn()) {
        AppContext.appRouter().build(RouterPath.Account.PATH)
            .navigation(this, RouterPath.Account.REQUEST_CODE)
        return false
    }
    return true
}

fun androidx.fragment.app.Fragment.requireLoggedIn() = activity?.requireLoggedIn() ?: false
