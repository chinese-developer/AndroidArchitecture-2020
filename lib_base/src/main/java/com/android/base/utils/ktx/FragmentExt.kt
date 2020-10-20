package com.android.base.utils.ktx

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


@JvmOverloads
fun Fragment.exitFragment(immediate: Boolean = false) {
    activity.exitFragment(immediate)
}

@JvmOverloads
fun FragmentActivity?.exitFragment(immediate: Boolean = false) {
    if (this == null) {
        return
    }
    val supportFragmentManager = this.supportFragmentManager
    val backStackEntryCount = supportFragmentManager.backStackEntryCount
    if (backStackEntryCount > 0) {
        if (immediate) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            supportFragmentManager.popBackStack()
        }
    } else {
        this.supportFinishAfterTransition()
    }
}

/** 实例化一个Fragment */
inline fun <reified F : Fragment> Context.newFragment(vararg args: Pair<String, String>): F {
    val bundle = Bundle()
    args.let {
        for (arg in args) {
            bundle.putString(arg.first, arg.second)
        }
    }
    @Suppress("DEPRECATION")
    return Fragment.instantiate(this, F::class.java.name, bundle) as F
}

/** 实例化一个Fragment */
inline fun <reified F : Fragment> Fragment.newFragment(vararg args: Pair<String, String>): F {
    val bundle = Bundle()
    args.let {
        for (arg in args) {
            bundle.putString(arg.first, arg.second)
        }
    }
    @Suppress("DEPRECATION")
    return Fragment.instantiate(context!!, F::class.java.name, bundle) as F
}

