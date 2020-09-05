/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import com.app.base.widget.dialog.mdstyle.MaterialDialog

@RestrictTo(LIBRARY_GROUP)
object MDUtil {

    @RestrictTo(LIBRARY_GROUP) fun <T : View> T.dimenPx(@DimenRes res: Int): Int {
        return context.resources.getDimensionPixelSize(res)
    }

    @RestrictTo(LIBRARY_GROUP) @ColorInt
    fun resolveColor(
      context: Context,
      @ColorRes res: Int? = null,
      @AttrRes attr: Int? = null,
      fallback: (() -> Int)? = null
    ): Int {
        if (attr != null) {
            context.theme.obtainStyledAttributes(intArrayOf(attr))
                .use {
                    val result = it.getColor(0, 0)
                    if (result == 0 && fallback != null) {
                        return fallback()
                    }
                    return result
                }
        }
        return ContextCompat.getColor(context, res ?: 0)
    }

    @RestrictTo(LIBRARY_GROUP) fun resolveString(
        materialDialog: MaterialDialog,
        @StringRes res: Int? = null,
        @StringRes fallback: Int? = null,
        html: Boolean = false
    ): CharSequence? = resolveString(
      context = materialDialog.windowContext,
      res = res,
      fallback = fallback,
      html = html
    )

    @RestrictTo(LIBRARY_GROUP) fun resolveString(
      context: Context,
      @StringRes res: Int? = null,
      @StringRes fallback: Int? = null,
      html: Boolean = false
    ): CharSequence? {
        val resourceId = res ?: (fallback ?: 0)
        if (resourceId == 0) return null
        val text = context.resources.getText(resourceId)
        if (html) {
            @Suppress("DEPRECATION")
            return Html.fromHtml(text.toString())
        }
        return text
    }

    @RestrictTo(LIBRARY_GROUP)
    fun resolveColors(
      context: Context,
      attrs: IntArray,
      fallback: ((forAttr: Int) -> Int)? = null
    ): IntArray {
        context.theme.obtainStyledAttributes(attrs)
            .use {
                return (attrs.indices).map { index ->
                    val color = it.getColor(index, 0)
                    return@map if (color != 0) {
                        color
                    } else {
                        fallback?.invoke(attrs[index]) ?: 0
                    }
                }
                    .toIntArray()
            }
    }

    @RestrictTo(LIBRARY_GROUP) fun resolveDrawable(
      context: Context,
      @DrawableRes res: Int? = null,
      @AttrRes attr: Int? = null,
      fallback: Drawable? = null
    ): Drawable? {
        if (attr != null) {
            context.theme.obtainStyledAttributes(intArrayOf(attr))
                .use {
                    var d = it.getDrawable(0)
                    if (d == null && fallback != null) {
                        d = fallback
                    }
                    return d
                }
        }
        if (res == null) return fallback
        return ContextCompat.getDrawable(context, res)
    }

    @RestrictTo(LIBRARY_GROUP) fun resolveDimen(
      context: Context,
      @AttrRes attr: Int,
      defaultValue: Float = 0f
    ): Float {
        context.theme.obtainStyledAttributes(intArrayOf(attr))
            .use {
                return it.getDimension(0, defaultValue)
            }
    }

    @RestrictTo(LIBRARY_GROUP) fun Int.isColorDark(threshold: Double = 0.5): Boolean {
        if (this == Color.TRANSPARENT) {
            return false
        }
        val darkness =
            1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
        return darkness >= threshold
    }

    @RestrictTo(LIBRARY_GROUP) fun resolveInt(
      context: Context,
      @AttrRes attr: Int,
      defaultValue: Int = 0
    ): Int {
        context.theme.obtainStyledAttributes(intArrayOf(attr))
            .use {
                return it.getInt(0, defaultValue)
            }
    }

    @RestrictTo(LIBRARY_GROUP) inline fun Int?.ifNotZero(block: (value: Int) -> Unit) {
        if (this != null && this != 0) {
            block(this)
        }
    }

    @RestrictTo(LIBRARY_GROUP) fun WindowManager.getWidthAndHeight(): Pair<Int, Int> {
        val size = Point()
        defaultDisplay.getSize(size)
        return Pair(size.x, size.y)
    }

    @RestrictTo(LIBRARY_GROUP) fun assertOneSet(
      method: String,
      b: Any?,
      a: Int?
    ) {
        require(
          !(a == null && b == null)
        ) { "$method: You must specify a resource ID or literal value" }
    }

    @RestrictTo(LIBRARY_GROUP) fun <T : View> T.waitForWidth(block: T.() -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            block()
            return
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
          var lastWidth: Int? = null

          override fun onGlobalLayout() {
            if (lastWidth != null && lastWidth == measuredWidth) {
              viewTreeObserver.removeOnGlobalLayoutListener(this)
              return
            }
            if (measuredWidth > 0 && measuredHeight > 0 && lastWidth != measuredWidth) {
              lastWidth = measuredWidth
              this@waitForWidth.block()
            }
          }
        })
    }

    @RestrictTo(LIBRARY_GROUP) fun <T : View> T.waitForHeight(block: T.() -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            this.block()
            return
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
          var lastHeight: Int? = null

          override fun onGlobalLayout() {
            if (lastHeight != null && lastHeight == measuredHeight) {
              viewTreeObserver.removeOnGlobalLayoutListener(this)
              return
            }
            if (measuredWidth > 0 && measuredHeight > 0 && lastHeight != measuredHeight) {
              lastHeight = measuredHeight
              this@waitForHeight.block()
            }
          }
        })
    }

    @RestrictTo(LIBRARY_GROUP) fun TextView?.maybeSetTextColor(
      context: Context,
      @AttrRes attrRes: Int?,
      @AttrRes hintAttrRes: Int? = null
    ) {
        if (this == null || (attrRes == null && hintAttrRes == null)) return
        if (attrRes != null) {
            resolveColor(context, attr = attrRes)
                .ifNotZero(this::setTextColor)
        }
        if (hintAttrRes != null) {
            resolveColor(context, attr = hintAttrRes)
                .ifNotZero(this::setHintTextColor)
        }
    }

    @RestrictTo(LIBRARY_GROUP) fun TextView?.maybeSetTextColor(
      context: Context,
      @ColorRes colorId: Int? = null,
      @AttrRes attrRes: Int? = null,
      @AttrRes hintAttrRes: Int? = null
    ) {
        if (this == null || (colorId == null && attrRes == null && hintAttrRes == null)) return
        if (colorId != null) {
            resolveColor(context, res = colorId)
                .ifNotZero(this::setTextColor)
        } else {
            if (attrRes != null) {
                resolveColor(context, attr = attrRes)
                    .ifNotZero(this::setTextColor)
            }
        }
        if (hintAttrRes != null) {
            resolveColor(context, attr = hintAttrRes)
                .ifNotZero(this::setHintTextColor)
        }
    }

    @RestrictTo(LIBRARY_GROUP) fun Context.getStringArray(@ArrayRes res: Int?): Array<String> {
        return if (res != null) resources.getStringArray(res) else emptyArray()
    }

    @RestrictTo(LIBRARY_GROUP) fun EditText.textChanged(callback: (CharSequence) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
          override fun afterTextChanged(s: Editable) = Unit

          override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
          ) = Unit

          override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
          ) = callback.invoke(s)
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    internal fun <T : View> T.isRtl(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) false else resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    @SuppressLint("ObsoleteSdkInt")
    internal fun TextView.setGravityStartCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        }
        this.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }

    @SuppressLint("ObsoleteSdkInt")
    internal fun TextView.setGravityEndCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }
        this.gravity = Gravity.END or Gravity.CENTER_VERTICAL
    }
}