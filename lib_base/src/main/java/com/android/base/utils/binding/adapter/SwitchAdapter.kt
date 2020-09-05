/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.widget.Switch
import androidx.databinding.BindingAdapter

object SwitchAdapter {

    @JvmStatic
    @BindingAdapter("android:switchTextAppearance")
    fun setSwitchTextAppearance(view: Switch, value: Int) {
        view.setSwitchTextAppearance(null, value)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:thumb"])
    fun setThumbResource(switch: Switch, drawableRes: Int) {
        if (drawableRes <= 0) {
            return
        }
        switch.setThumbResource(drawableRes)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:track"])
    fun setTrackResource(switch: Switch, trackRes: Int) {
        if (trackRes <= 0) {
            return
        }
        switch.setTrackResource(trackRes)
    }

}