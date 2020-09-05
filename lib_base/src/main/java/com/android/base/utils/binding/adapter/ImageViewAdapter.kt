/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object ImageViewAdapter {

    /**
     * 设置drawable 文件夹中的图片本地资源图片
     * @param resID res id
     */
    @JvmStatic
    @BindingAdapter(value = ["android:src"], requireAll = false)
    fun setImageRes(imageView: ImageView, resID: Int) {
        if (resID <= 0) {
            imageView.setImageResource(0)
        }
        imageView.setImageResource(resID)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:tint"])
    fun setImageTintList(imageView: ImageView, color: Int) {
        if (color <= 0) {
            return
        }
        imageView.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(imageView.context, color))
    }

    @JvmStatic
    @BindingAdapter(value = ["android:tintMode"])
    fun setImageTintMode(imageView: ImageView, tintMode: PorterDuff.Mode) {
        imageView.imageTintMode = tintMode
    }
}