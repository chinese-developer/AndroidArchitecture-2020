/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.android.base.utils.android.views.dpToPx

@BindingMethods(
        BindingMethod(type = View::class, attribute = "android:fadeScrollbars", method = "setScrollbarFadingEnabled"),
        BindingMethod(type = View::class, attribute = "android:getOutline", method = "setOutlineProvider"),
        BindingMethod(type = View::class, attribute = "android:nextFocusForward", method = "setNextFocusForwardId"),
        BindingMethod(type = View::class, attribute = "android:nextFocusLeft", method = "setNextFocusLeftId"),
        BindingMethod(type = View::class, attribute = "android:nextFocusRight", method = "setNextFocusRightId"),
        BindingMethod(type = View::class, attribute = "android:nextFocusUp", method = "setNextFocusUpId"),
        BindingMethod(type = View::class, attribute = "android:nextFocusDown", method = "setNextFocusDownId"),
        BindingMethod(type = View::class, attribute = "android:requiresFadingEdge", method = "setVerticalFadingEdgeEnabled"),
        BindingMethod(type = View::class, attribute = "android:scrollbarDefaultDelayBeforeFade", method = "setScrollBarDefaultDelayBeforeFade"),
        BindingMethod(type = View::class, attribute = "android:scrollbarFadeDuration", method = "setScrollBarFadeDuration"),
        BindingMethod(type = View::class, attribute = "android:scrollbarSize", method = "setScrollBarSize"),
        BindingMethod(type = View::class, attribute = "android:scrollbarStyle", method = "setScrollBarStyle"),
        BindingMethod(type = View::class, attribute = "android:transformPivotX", method = "setPivotX"),
        BindingMethod(type = View::class, attribute = "android:transformPivotY", method = "setPivotY"),
        BindingMethod(type = View::class, attribute = "android:onDrag", method = "setOnDragListener"),
        BindingMethod(type = View::class, attribute = "android:onClick", method = "setOnclickListener"),
        BindingMethod(type = View::class, attribute = "android:onApplyWindowInsets", method = "setOnApplyWindowInsetsListener"),
        BindingMethod(type = View::class, attribute = "android:onCreateContextMenu", method = "setOnCreateContextMenuListener"),
        BindingMethod(type = View::class, attribute = "android:onGenericMotion", method = "setOnGenericMotionListener"),
        BindingMethod(type = View::class, attribute = "android:onHover", method = "setOnHoverListener"),
        BindingMethod(type = View::class, attribute = "android:onKey", method = "setOnKeyListener"),
        BindingMethod(type = View::class, attribute = "android:onLongClick", method = "setOnLongClickListener"),
        BindingMethod(type = View::class, attribute = "android:onSystemUiVisibilityChange", method = "setOnSystemUiVisibilityChangeListener"),
        BindingMethod(type = View::class, attribute = "android:onTouch", method = "setOnTouchListener"),
        BindingMethod(type = View::class, attribute = "android:enabled", method = "setEnabled")
)
object ViewBindingAdapter {
    var FADING_EDGE_NONE = 0
    private var FADING_EDGE_HORIZONTAL = 1
    private var FADING_EDGE_VERTICAL = 2

    @JvmStatic
    @BindingAdapter("android:padding")
    fun setPadding(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPadding(padding, padding, padding, padding)
    }

    @JvmStatic
    @BindingAdapter("android:paddingBottom")
    fun setPaddingBottom(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, padding)
    }

    @JvmStatic
    @BindingAdapter("android:paddingEnd")
    fun setPaddingEnd(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPaddingRelative(view.paddingStart, view.paddingTop, padding, view.paddingBottom)
    }

    @JvmStatic
    @BindingAdapter("android:paddingLeft")
    fun setPaddingLeft(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPadding(padding, view.paddingTop, view.paddingRight, view.paddingBottom)
    }

    @JvmStatic
    @BindingAdapter("android:paddingRight")
    fun setPaddingRight(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPadding(view.paddingLeft, view.paddingTop, padding, view.paddingBottom)
    }

    @JvmStatic
    @BindingAdapter("android:paddingStart")
    fun setPaddingStart(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPaddingRelative(padding, view.paddingTop, view.paddingEnd, view.paddingBottom)
    }

    @JvmStatic
    @BindingAdapter("android:paddingTop")
    fun setPaddingTop(view: View, paddingFloat: Float) {
        val padding = pixelsToDimensionPixelSize(paddingFloat)
        view.setPadding(view.paddingLeft, padding, view.paddingRight, view.paddingBottom)
    }

    @JvmStatic
    @BindingAdapter("android:requiresFadingEdge")
    fun setRequiresFadingEdge(view: View, value: Int) {
        val vertical = value and FADING_EDGE_VERTICAL != 0
        val horizontal = value and FADING_EDGE_HORIZONTAL != 0
        view.isVerticalFadingEdgeEnabled = vertical
        view.isHorizontalFadingEdgeEnabled = horizontal
    }

    @JvmStatic
    @BindingAdapter("android:onLayoutChange")
    fun setOnLayoutChangeListener(view: View, oldValue: View.OnLayoutChangeListener?,
                                  newValue: View.OnLayoutChangeListener?) {
        oldValue?.apply {
            view.removeOnLayoutChangeListener(this)
        }
        newValue?.apply {
            view.addOnLayoutChangeListener(newValue)
        }
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(view: View, resId: Int) {
        if (resId <= 0) {
            return
        }
        view.setBackgroundResource(resId)
    }

    @JvmStatic
    @BindingAdapter("android:backgroundTint")
    fun setBackgroundTintColor(view: View, resId: Int) {
        if (resId <= 0) {
            return
        }
        view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, resId))
    }

    /** 动态设置宽度 */
    @JvmStatic
    @BindingAdapter("android:layout_width")
    fun setViewWidth(view: View, width: Int) {
        val layoutParams = view.layoutParams
        if (width >= 0) {
            layoutParams.width = dpToPx(width)
            view.layoutParams = layoutParams
        }
    }

    /** 动态设置高度 */
    @JvmStatic
    @BindingAdapter("android:layout_height")
    fun setViewHeight(view: View, height: Int) {
        val layoutParams = view.layoutParams
        if (height >= 0) {
            layoutParams.height = dpToPx(height)
            view.layoutParams = layoutParams
        }
    }

    /** 动态设置View的宽度 只针对LinearLayout */
    @JvmStatic
    @BindingAdapter("android:layout_weight")
    fun setWeight(view: View, weight: Float) {
        if (weight < 0) return
        val layoutParams = view.layoutParams
        if (layoutParams is LinearLayout.LayoutParams) {
            layoutParams.weight = weight
            view.layoutParams = layoutParams
        }
    }


    // Follows the same conversion mechanism as in TypedValue.complexToDimensionPixelSize as used
    // when setting padding. It rounds off the float value unless the value is < 1.
    // When a value is between 0 and 1, it is set to 1. A value less than 0 is set to -1.
    private fun pixelsToDimensionPixelSize(pixels: Float): Int {
        val result = (pixels + 0.5f).toInt()
        return when {
            result != 0 -> {
                result
            }
            pixels == 0f -> {
                0
            }
            pixels > 0 -> {
                1
            }
            else -> {
                -1
            }
        }
    }
}