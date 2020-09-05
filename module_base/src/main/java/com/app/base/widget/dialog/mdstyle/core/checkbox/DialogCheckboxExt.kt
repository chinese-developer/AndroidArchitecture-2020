/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.checkbox

import android.view.View
import android.widget.CheckBox
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.core.widget.CompoundButtonCompat
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.maybeSetTextColor
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveString
import com.app.base.widget.dialog.mdstyle.util.createColorSelector
import com.app.base.widget.dialog.mdstyle.util.resolveColors

typealias BooleanCallback = ((Boolean) -> Unit)?

@CheckResult fun MaterialDialog.getCheckboxPrompt(): CheckBox {
    return view.buttonsLayout?.checkBoxPrompt ?: throw IllegalStateException(
      "The dialog does not have an attached buttons layout."
    )
}

@JvmOverloads
fun MaterialDialog.checkBoxPrompt(
  @StringRes res: Int? = null,
  text: CharSequence? = null,
  isCheckedDefault: Boolean = false,
  onToggle: BooleanCallback
): MaterialDialog {
    assertOneSet("checkBoxPrompt", text, res)
    view.buttonsLayout?.checkBoxPrompt?.run {
        this.visibility = View.VISIBLE
        this.text = text ?: resolveString(this@checkBoxPrompt, res)
        this.isChecked = isCheckedDefault
        this.setOnCheckedChangeListener { _, isChecked ->
            onToggle?.invoke(isChecked)
        }

        maybeSetTextColor(windowContext, attrRes = R.attr.md_color_content)
        bodyFont?.let(this::setTypeface)

        val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
        resolveColors(attrs = widgetAttrs).let {
            CompoundButtonCompat.setButtonTintList(
              this,
              createColorSelector(context, checked = it[0], unchecked = it[1])
            )
        }
    }
    return this
}