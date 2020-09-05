/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spanned
import android.text.method.DialerKeyListener
import android.text.method.DigitsKeyListener
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

@BindingMethods(
        BindingMethod(type = TextView::class, attribute = "android:autoLink", method = "setAutoLinkMask"),
        BindingMethod(type = TextView::class, attribute = "android:drawablePadding", method = "setCompoundDrawablePadding"),
        BindingMethod(type = TextView::class, attribute = "android:editorExtras", method = "setInputExtras"),
        BindingMethod(type = TextView::class, attribute = "android:inputType", method = "setRawInputType"),
        BindingMethod(type = TextView::class, attribute = "android:scrollHorizontally", method = "setHorizontallyScrolling"),
        BindingMethod(type = TextView::class, attribute = "android:textAllCaps", method = "setAllCaps"),
        BindingMethod(type = TextView::class, attribute = "android:textColorHighlight", method = "setHighlightColor"),
        BindingMethod(type = TextView::class, attribute = "android:textColorHint", method = "setHintTextColor"),
        BindingMethod(type = TextView::class, attribute = "android:textColorLink", method = "setLinkTextColor"),
        BindingMethod(type = TextView::class, attribute = "android:onEditorAction", method = "setOnEditorActionListener"),
        BindingMethod(type = TextView::class, attribute = "android:textStyle", method = "setTypeface")
)

object TextViewAdapter {

    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: TextView, text: CharSequence?) {
        val oldText = view.text
        if (text === oldText || text == null) {
            return
        }
        if (text is Spanned) {
            if (text == oldText) {
                return  // No change in the spans, so don't set anything.
            }
        } else if (!haveContentsChanged(text, oldText)) {
            return  // No content changes, so don't set anything.
        }
        view.text = text
    }

    @JvmStatic
    @BindingAdapter("android:textColor")
    fun setTextColor(view: TextView, color: Int) {
        if (color <= 0) {
            return
        }
        view.setTextColor(ContextCompat.getColor(view.context, color))
    }


    @JvmStatic
    private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {
        if (str1 == null != (str2 == null)) {
            return true
        } else if (str1 == null) {
            return false
        }
        val length = str1.length
        if (length != str2!!.length) {
            return true
        }
        for (i in 0 until length) {
            if (str1[i] != str2[i]) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    @BindingAdapter("android:digits")
    fun setDigits(view: TextView, digits: CharSequence?) {
        if (digits != null) {
            view.keyListener = DigitsKeyListener.getInstance(digits.toString())
        } else if (view.keyListener is DigitsKeyListener) {
            view.keyListener = null
        }
    }

    @JvmStatic
    @BindingAdapter("android:phoneNumber")
    fun setPhoneNumber(view: TextView, phoneNumber: Boolean) {
        if (phoneNumber) {
            view.keyListener = DialerKeyListener.getInstance()
        } else if (view.keyListener is DialerKeyListener) {
            view.keyListener = null
        }
    }

    @JvmStatic
    @BindingAdapter("android:textSize")
    fun setTextSizeFloat(view: TextView, size: Float) {
        if (size <= 0) {
            return
        }
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }


    @JvmStatic
    @BindingAdapter("android:textSize")
    fun setTextSizeInt(view: TextView, size: Int) {
        if (size <= 0) {
            return
        }
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
    }


    @JvmStatic
    @BindingAdapter("android:drawableBottom")
    fun setDrawableBottom(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawables
        view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable)
    }

    @JvmStatic
    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawables
        view.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3])
    }

    @JvmStatic
    @BindingAdapter("android:drawableRight")
    fun setDrawableRight(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawables
        view.setCompoundDrawables(drawables[0], drawables[1], drawable,
                drawables[3])
    }

    @JvmStatic
    @BindingAdapter("android:drawableTop")
    fun setDrawableTop(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawables
        view.setCompoundDrawables(drawables[0], drawable, drawables[2],
                drawables[3])
    }

    @JvmStatic
    @BindingAdapter("android:drawableStart")
    fun setDrawableStart(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawablesRelative
        view.setCompoundDrawablesRelative(drawable, drawables[1], drawables[2], drawables[3])
    }

    @JvmStatic
    @BindingAdapter("android:drawableEnd")
    fun setDrawableEnd(view: TextView, drawable: Drawable?) {
        setIntrinsicBounds(drawable)
        val drawables = view.compoundDrawablesRelative
        view.setCompoundDrawablesRelative(drawables[0], drawables[1], drawable, drawables[3])
    }

    @JvmStatic
    private fun setIntrinsicBounds(drawable: Drawable?) {
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    }


    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    @BindingAdapter("android:maxLength")
    fun setMaxLength(view: TextView, value: Int) {
        if (value <= 0) {
            return
        }
        var filters = view.filters
        if (filters == null) {
            filters = arrayOf<InputFilter?>(
                    LengthFilter(value)
            )
        } else {
            var foundMaxLength = false
            for (i in filters.indices) {
                val filter = filters[i]
                if (filter is LengthFilter) {
                    foundMaxLength = true
                    var replace = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        replace = filter.max != value
                    }
                    if (replace) {
                        filters[i] = LengthFilter(value)
                    }
                    break
                }
            }
            if (!foundMaxLength) {
                // can't use Arrays.copyOf -- it shows up in API 9
                val oldFilters = filters
                filters = arrayOfNulls(oldFilters.size + 1)
                System.arraycopy(oldFilters, 0, filters, 0, oldFilters.size)
                filters[filters.size - 1] = LengthFilter(value)
            }
        }
        view.filters = filters
    }
}