/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.widget.CompoundButton
import androidx.databinding.*

@BindingMethods(
        BindingMethod(type = CompoundButton::class, attribute = "android:buttonTint", method = "setButtonTintList"),
        BindingMethod(type = CompoundButton::class, attribute = "android:onCheckedChanged", method = "setOnCheckedChangeListener")
)
@InverseBindingMethods(
        InverseBindingMethod(type = CompoundButton::class, attribute = "android:checked")
)
object CompoundButtonBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:checked")
    fun setChecked(view: CompoundButton, checked: Boolean) {
        if (view.isChecked != checked) {
            view.isChecked = checked
        }
    }
}