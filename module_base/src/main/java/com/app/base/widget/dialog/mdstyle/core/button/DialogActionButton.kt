/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import com.android.base.utils.ktx.no
import com.android.base.utils.ktx.otherwise
import com.app.base.widget.dialog.mdstyle.inferThemeIsLight
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.ifNotZero
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColor
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveDrawable
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveInt
import com.app.base.widget.dialog.mdstyle.util.MDUtil.setGravityEndCompat
import com.app.base.widget.dialog.mdstyle.util.adjustAlpha

/**
 *@author Nemo
 *      Email: nemo@seektop.com
 *      Date : 2019-10-26 15:57
 *
 *      包含一组三个按钮（agree、disAgree、idk）以及一个 checkbox
 */
class DialogActionButton(
  context: Context,
  attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
  companion object {
    private const val CASING_UPPER = 1
  }

  private var enabledColor: Int = 0
  private var disabledColor: Int = 0

  init {
    isClickable = true
    isFocusable = true
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    setTextColor(if (enabled) enabledColor else disabledColor)
  }

  fun updateTextColor(@ColorInt color: Int) {
    enabledColor = color
    isEnabled = isEnabled
  }

  internal fun update(
    baseContext: Context,
    appContext: Context,
    stacked: Boolean,
    id: Int
  ) {
    // Casing
    val casing = resolveInt(
        context = appContext,
        attr = R.attr.md_button_casing,
        defaultValue = CASING_UPPER
    )

    setSupportAllCaps(casing == CASING_UPPER)

    // Text color
    val isLightTheme = inferThemeIsLight(appContext)

    val disabledColorRes =
      if (isLightTheme) R.color.md_disabled_text_light_theme
      else R.color.md_disabled_text_dark_theme
    disabledColor = resolveColor(baseContext, res = disabledColorRes)

    currentTextColor.hasColorValue()
        .no {
          when (id) {
            R.id.md_button_positive -> {
              enabledColor = resolveColor(appContext, attr = R.attr.md_color_positive_button_text) {
                resolveColor(appContext, attr = R.attr.colorPrimary)
              }
            }
            R.id.md_button_negative -> {
              enabledColor = resolveColor(appContext, attr = R.attr.md_color_negative_button_text) {
                resolveColor(appContext, attr = R.attr.colorPrimary)
              }
            }
            R.id.md_button_neutral -> {
              enabledColor = resolveColor(appContext, attr = R.attr.md_color_neutral_button_text) {
                resolveColor(appContext, attr = R.attr.colorPrimary)
              }
            }
            else -> {
            }
          }
        }
        .otherwise { if (currentTextColor != disabledColor) enabledColor = this.currentTextColor }

    setTextColor(enabledColor)

    // Selector
    val bgDrawable = resolveDrawable(baseContext, attr = R.attr.md_button_selector)
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && bgDrawable is RippleDrawable) {
      resolveColor(context = baseContext, attr = R.attr.md_ripple_color) {
        resolveColor(appContext, attr = R.attr.colorPrimary).adjustAlpha(.12f)
      }.ifNotZero {
        bgDrawable.setColor(ColorStateList.valueOf(it))
      }
    }

    // background
    background = bgDrawable

    // Text alignment
    if (stacked) setGravityEndCompat()
    else gravity = CENTER

    // 如果在执行此方法之前更改了状态为 enabled = true ，则无效
    isEnabled = isEnabled
  }

  private fun Int.hasColorValue() = this != 0
}