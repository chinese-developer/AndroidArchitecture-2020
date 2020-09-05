package com.app.base.widget.dialog.mdstyle.util

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun <T : Drawable> T.tintDrawable(tint: Int): Drawable? {
    val drawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(drawable, tint)
    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
    return drawable
}

internal fun Drawable.tint(@ColorInt color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}