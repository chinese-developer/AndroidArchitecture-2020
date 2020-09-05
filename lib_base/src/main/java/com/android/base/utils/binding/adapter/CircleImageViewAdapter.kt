/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import de.hdodenhof.circleimageview.CircleImageView

@BindingMethods(
        BindingMethod(type = CircleImageView::class, attribute = "app:civ_border_width", method = "setBorderWidth"),
        BindingMethod(type = CircleImageView::class, attribute = "app:civ_border_color", method = "setBorderColor"),
        BindingMethod(type = CircleImageView::class, attribute = "app:civ_circle_background_color", method = "setCircleBackgroundColor")
)

object CircleImageViewAdapter