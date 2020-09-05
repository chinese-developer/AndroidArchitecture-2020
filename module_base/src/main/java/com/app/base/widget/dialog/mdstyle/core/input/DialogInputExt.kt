/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.input

import android.text.InputType
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.hasActionButtons
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.core.callbacks.onPreShow
import com.app.base.widget.dialog.mdstyle.core.callbacks.onShow
import com.app.base.widget.dialog.mdstyle.core.customview.customView
import com.app.base.widget.dialog.mdstyle.core.customview.getCustomView
import com.app.base.widget.dialog.mdstyle.util.MDUtil.maybeSetTextColor
import com.app.base.widget.dialog.mdstyle.util.MDUtil.textChanged
import com.google.android.material.textfield.TextInputLayout

typealias InputCallback = ((MaterialDialog, CharSequence) -> Unit)?

/**
 * 带 Input MD Dialog. 可以和 Title、 Message 和 checkBoxPrompt 一起使用，但不能与列表一起使用。
 *
 * @param hint 提示文字
 * @param hintRes 提示文字资源文件
 * @param preFill 预填充文字
 * @param preFillRes 预填充文字资源文件
 * @param inputType 键盘类型
 * @param maxLength 最长可输入的文字长度
 * @param waitForPositiveButton 在单击 positive 按钮之前，不会调用[callback]。否则，每次输入文本更改时都会调用它。
 * @param allowEmpty 是否允许为空，默认是不允许的，并禁用 positive 按钮，直至检测到有文字输入。
 * @param callback 文字输入监听回调，如果 waitForPositiveButton = false，则每次文字输入后都会收到回调。反之，只有点击 positive 按钮后才会收到回调。
 */
@JvmOverloads
fun MaterialDialog.input(
  hint: String? = null,
  @StringRes hintRes: Int? = null,
  preFill: CharSequence? = null,
  @StringRes preFillRes: Int? = null,
  inputType: Int = InputType.TYPE_CLASS_TEXT,
  maxLength: Int? = null,
  waitForPositiveButton: Boolean = true,
  allowEmpty: Boolean = false,
  callback: InputCallback = null
): MaterialDialog {
    customView(R.layout.md_dialog_stub_input)
    onPreShow { showKeyboardIfApplicable() }
    if (!hasActionButtons()) {
        positiveButton()
    }
    if (callback != null && waitForPositiveButton) {
        positiveButton { callback.invoke(this@input, getInputField().text ?: "") }
    }

    prefillInput(preFill = preFill, preFillRes = preFillRes, allowEmpty = allowEmpty)
    styleInput(hint = hint, hintRes = hintRes, inputType = inputType)

    if (maxLength != null) {
        getInputLayout().run {
            isCounterEnabled = true
            counterMaxLength = maxLength
        }
        invalidateInputMaxLength(allowEmpty)
    }

    getInputField().textChanged {
        if (!allowEmpty) {
            setActionButtonEnabled(POSITIVE, it.isNotEmpty())
        }
        maxLength?.let { invalidateInputMaxLength(allowEmpty) }

        if (!waitForPositiveButton && callback != null) {
            callback.invoke(this, it)
        }
    }

    return this
}

@CheckResult fun MaterialDialog.getInputLayout(): TextInputLayout {
    return getCustomView().findViewById(R.id.md_input_layout) as? TextInputLayout
        ?: throw IllegalStateException("You have not setup this dialog as an input dialog.")
}

@CheckResult fun MaterialDialog.getInputField(): EditText {
    return getInputLayout().editText ?: throw IllegalStateException(
      "You have not setup this dialog as an input dialog."
    )
}

private fun MaterialDialog.prefillInput(
  preFill: CharSequence?,
  preFillRes: Int?,
  allowEmpty: Boolean
) {
    val resources = windowContext.resources
    val editText = getInputField()

    val preFillText = preFill ?: if (preFillRes != null) resources.getString(preFillRes) else ""
    if (preFillText.isNotEmpty()) {
        editText.setText(preFillText)
        onShow { editText.setSelection(preFillText.length) }
    }
    setActionButtonEnabled(
      POSITIVE,
      allowEmpty || preFillText.isNotEmpty()
    )
}

private fun MaterialDialog.styleInput(
  hint: String?,
  hintRes: Int?,
  inputType: Int
) {
    val resources = windowContext.resources
    val editText = getInputField()

    editText.hint = hint ?: if (hintRes != null) resources.getString(hintRes) else null
    editText.inputType = inputType
    editText.maybeSetTextColor(
      windowContext,
      attrRes = R.attr.md_color_content,
      hintAttrRes = R.attr.md_color_hint
    )
    bodyFont?.let(editText::setTypeface)
}