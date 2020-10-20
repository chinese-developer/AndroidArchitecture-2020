@file:JvmName("Resources")

package com.android.base.utils.ktx

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.*

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.base.utils.BaseUtils

fun Fragment.getColorRes(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun Fragment.getDrawableRes(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun View.getColorRes(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun View.getDrawableRes(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun Context.getColorRes(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getDrawableRes(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

/**
 * - [name] 资源的名称，如 acc_ic_launcher 或者 com.example.android/drawable/acc_ic_launcher(这是，下面两个参数可以省略)
 * - [defType] 资源的类型，如 drawable
 * - [defPackage] 包名
 *
 * 返回资源 id
 */
fun getResource(name: String, defType: String, defPackage: String): Int {
    return BaseUtils.getResources().getIdentifier(name, defType, defPackage)
}

fun getText(@StringRes id: Int): CharSequence {
    return BaseUtils.getResources().getText(id)
}

fun getString(@StringRes id: Int): String {
    return BaseUtils.getResources().getString(id)
}

fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return BaseUtils.getResources().getString(id, *formatArgs)
}

fun getStringArray(@ArrayRes id: Int): Array<String> {
    return BaseUtils.getResources().getStringArray(id)
}

fun getIntArray(@ArrayRes id: Int): IntArray {
    return BaseUtils.getResources().getIntArray(id)
}

fun createUriByResource(id: Int): Uri {
    return Uri.parse("android.resource://" + BaseUtils.getAppContext().packageName + "/" + id)
}

fun createUriByAssets(path: String): Uri {
    return Uri.parse("file:///android_asset/$path")
}

fun getDimensionPixelSize(dimenId: Int): Int {
    return BaseUtils.getResources().getDimensionPixelSize(dimenId)
}