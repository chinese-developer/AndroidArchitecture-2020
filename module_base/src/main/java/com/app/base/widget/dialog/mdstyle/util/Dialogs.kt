/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

import android.app.Dialog
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.callbacks.invokeAll
import com.app.base.widget.dialog.mdstyle.core.checkbox.getCheckboxPrompt
import com.app.base.widget.dialog.mdstyle.core.customview.CUSTOM_VIEW_NO_VERTICAL_PADDING
import com.app.base.widget.dialog.mdstyle.core.lifecycle.DialogLifecycleObserver
import com.app.base.widget.dialog.mdstyle.util.MDUtil.maybeSetTextColor
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveDrawable
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveString

@RestrictTo(LIBRARY_GROUP)
fun MaterialDialog.invalidateDividers(
    showTop: Boolean,
    showBottom: Boolean
): Unit = view.invalidateDividers(showTop, showBottom)

internal fun MaterialDialog.populateIcon(
    imageView: ImageView,
    @DrawableRes iconRes: Int?,
    icon: Drawable?
) {
    val drawable = resolveDrawable(windowContext, res = iconRes, fallback = icon)
    if (drawable != null) {
        (imageView.parent as View).visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        imageView.setImageDrawable(drawable)
    } else {
        imageView.visibility = View.GONE
    }
}

internal fun MaterialDialog.populateText(
    textView: TextView,
    @StringRes textRes: Int? = null,
    text: CharSequence? = null,
    @StringRes fallback: Int = 0,
    typeface: Typeface?,
    textColor: Int? = null,
    @ColorRes colorId: Int? = null
) {
    val value = text ?: resolveString(this, textRes, fallback)
    if (value != null) {
        (textView.parent as View).visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        textView.text = value
        if (typeface != null) {
            textView.typeface = typeface
        }
        textView.maybeSetTextColor(windowContext, attrRes = textColor, colorId = colorId)
    } else {
        textView.visibility = View.GONE
    }
}

internal fun MaterialDialog.preShow() {
    val customViewNoVerticalPadding = config[CUSTOM_VIEW_NO_VERTICAL_PADDING] as? Boolean == false
    this.preShowListeners.invokeAll(this)

    this.view.run {
        if (titleLayout.shouldNotBeVisible() && !customViewNoVerticalPadding) {
            // 如果没有标题或按钮，应当减少顶部与底部的 padding
            contentLayout.modifyFirstAndLastPadding(
                top = frameMarginVertical,
                bottom = frameMarginVertical
            )
        }
        if (getCheckboxPrompt().isVisibleForMd()) {
            // 如果有复选框，则清空底部填充 padding
            contentLayout.modifyFirstAndLastPadding(bottom = 0)
        } else if (contentLayout.haveMoreThanOneChild()) {
            contentLayout.modifyScrollViewPadding(bottom = frameMarginVerticalLess)
        }
    }
}

fun MaterialDialog.lifecycleOwner(owner: LifecycleOwner? = null): MaterialDialog {
    val observer = DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (windowContext as? LifecycleOwner
        ?: throw IllegalStateException(
            "$windowContext is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}

fun Dialog.lifecycleOwner(owner: LifecycleOwner? = null): Dialog {
    val observer = DialogLifecycleObserver(::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}