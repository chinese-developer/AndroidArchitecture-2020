/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

@BindingMethods(
    BindingMethod(
        type = Toolbar::class,
        attribute = "android:onMenuItemClick",
        method = "setOnMenuItemClickListener"
    ),
    BindingMethod(
        type = Toolbar::class,
        attribute = "android:onNavigationClick",
        method = "setNavigationOnClickListener"
    )
)

object ToolbarAdapter