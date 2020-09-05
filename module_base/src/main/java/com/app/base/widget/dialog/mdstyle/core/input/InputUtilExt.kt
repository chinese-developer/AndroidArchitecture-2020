/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.input

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled

internal fun MaterialDialog.showKeyboardIfApplicable() {
    getInputField().postRun {
        requestFocus()
        val imm =
            windowContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

internal inline fun <T : View> T.postRun(crossinline exec: T.() -> Unit) = this.post {
    this.exec()
}

internal fun MaterialDialog.invalidateInputMaxLength(allowEmpty: Boolean) {
    val currentLength = getInputField().text?.length ?: 0
    if (!allowEmpty && currentLength == 0) {
        return
    }
    val maxLength = getInputLayout().counterMaxLength
    if (maxLength > 0) {
        setActionButtonEnabled(POSITIVE, currentLength <= maxLength)
    }
}