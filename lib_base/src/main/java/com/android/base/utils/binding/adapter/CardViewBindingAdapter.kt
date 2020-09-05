/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import androidx.cardview.widget.CardView

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

@BindingMethods(
        BindingMethod(type = CardView::class, attribute = "cardCornerRadius", method = "setRadius"),
        BindingMethod(type = CardView::class, attribute = "cardMaxElevation", method = "setMaxCardElevation"),
        BindingMethod(type = CardView::class, attribute = "cardPreventCornerOverlap", method = "setPreventCornerOverlap"),
        BindingMethod(type = CardView::class, attribute = "cardUseCompatPadding", method = "setUseCompatPadding")
)

object CardViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("contentPadding")
    fun setContentPadding(view: CardView, padding: Int) {
        view.setContentPadding(padding, padding, padding, padding)
    }

    @JvmStatic
    @BindingAdapter("contentPaddingLeft")
    fun setContentPaddingLeft(view: CardView, left: Int) {
        val top = view.contentPaddingTop
        val right = view.contentPaddingRight
        val bottom = view.contentPaddingBottom
        view.setContentPadding(left, top, right, bottom)
    }

    @JvmStatic
    @BindingAdapter("contentPaddingTop")
    fun setContentPaddingTop(view: CardView, top: Int) {
        val left = view.contentPaddingLeft
        val right = view.contentPaddingRight
        val bottom = view.contentPaddingBottom
        view.setContentPadding(left, top, right, bottom)
    }

    @JvmStatic
    @BindingAdapter("contentPaddingRight")
    fun setContentPaddingRight(view: CardView, right: Int) {
        val left = view.contentPaddingLeft
        val top = view.contentPaddingTop
        val bottom = view.contentPaddingBottom
        view.setContentPadding(left, top, right, bottom)
    }

    @JvmStatic
    @BindingAdapter("contentPaddingBottom")
    fun setContentPaddingBottom(view: CardView, bottom: Int) {
        val left = view.contentPaddingLeft
        val top = view.contentPaddingTop
        val right = view.contentPaddingRight
        view.setContentPadding(left, top, right, bottom)
    }
}