/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.binding

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter

@BindingAdapter("goneUnless")
fun bindGoneUnless(view: View, gone: Boolean) {
    view.visibility = if (gone) {
        GONE
    } else {
        VISIBLE
    }
}

@BindingAdapter("visibleUnless")
fun bindVisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) {
        VISIBLE
    } else {
        GONE
    }
}

@BindingAdapter("alphaNightMode")
fun bindAlphaUnless(view: View, isNightMode: Boolean) {
    if (isNightMode) {
        view.alpha = 0.7f
    } else {
        view.alpha = 1.0f
    }
}

@BindingAdapter("loseFocusWhen")
fun loseFocusWhen(view: View, condition: Boolean) {
    if (condition) {
        view.clearFocus()
    }
}