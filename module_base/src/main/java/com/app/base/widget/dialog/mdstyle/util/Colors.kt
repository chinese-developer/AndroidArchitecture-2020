/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.app.base.widget.dialog.mdstyle.MaterialDialog

@ColorInt @CheckResult
internal fun Int.adjustAlpha(
    alpha: Float
): Int = Color.argb(
    (255 * alpha).toInt(),
    Color.red(this),
    Color.green(this),
    Color.blue(this)
)

@ColorInt @CheckResult
internal fun MaterialDialog.resolveColor(
    @ColorRes res: Int? = null,
    @AttrRes attr: Int? = null,
    fallback: (() -> Int)? = null
): Int = MDUtil.resolveColor(windowContext, res, attr, fallback)

@ColorInt @CheckResult
internal fun MaterialDialog.resolveColors(
    attrs: IntArray,
    fallback: ((forAttr: Int) -> Int)? = null
): IntArray = MDUtil.resolveColors(windowContext, attrs, fallback)