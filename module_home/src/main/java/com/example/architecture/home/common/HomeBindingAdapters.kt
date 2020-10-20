/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:JvmName("HomeBindingAdapters")

package com.example.architecture.home.common

import android.graphics.drawable.AnimationDrawable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.base.utils.ktx.setLeftDrawable
import com.example.architecture.home.R

@BindingAdapter("loadTextViewStartAnimWhen")
fun loadTextViewStartAnimWhen(textView: TextView, condition: Boolean) {
    if (condition) {
        textView.setLeftDrawable(R.drawable.loadanimation)
        (textView.compoundDrawables[0] as? AnimationDrawable?)?.start()
        textView.text = ""
    } else {
        textView.setLeftDrawable(0)
        val serialNumber = textView.text
        (textView.compoundDrawables[0] as? AnimationDrawable?)?.stop()
        textView.text = serialNumber.toString()
    }
}
