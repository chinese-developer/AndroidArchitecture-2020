/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.content.res.ColorStateList
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object ProgressBarBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:indeterminateTint"])
    fun setIndeterminateTintList(progressBar: ProgressBar, indeterminateTint: Int) {
        if (indeterminateTint <= 0) {
            return
        }
        progressBar.indeterminateTintList =
            ColorStateList.valueOf(ContextCompat.getColor(progressBar.context, indeterminateTint))
    }


    @JvmStatic
    @BindingAdapter(value = ["android:progressTint"])
    fun setProgressTintList(progressBar: ProgressBar, indeterminateTint: Int) {
        if (indeterminateTint <= 0) {
            return
        }
        progressBar.progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(progressBar.context, indeterminateTint))
    }

    @JvmStatic
    @BindingAdapter(value = ["android:secondaryProgressTint"])
    fun setSecondaryProgressTintList(progressBar: ProgressBar, secondaryProgressTint: Int) {
        if (secondaryProgressTint <= 0) {
            return
        }
        progressBar.secondaryProgressTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                progressBar.context,
                secondaryProgressTint
            )
        )
    }
}
