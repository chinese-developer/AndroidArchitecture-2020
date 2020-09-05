/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.actions

import com.android.base.utils.android.views.isVisible
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton
import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButton

fun MaterialDialog.getActionButton(which: WhichButton): DialogActionButton {
  return view.buttonsLayout?.actionButtons?.get(which.index) ?: throw IllegalStateException(
      "The dialog does not have an attached buttons layout."
  )
}

/** 当前 dialog 是否存在 actionButtons */
fun MaterialDialog.hasActionButtons(): Boolean {
  return view.buttonsLayout?.visibleButtons?.isNotEmpty() ?: false
}

fun MaterialDialog.hasActionButtons(which: WhichButton) = getActionButton(which).isVisible()

fun MaterialDialog.setActionButtonEnabled(
  which: WhichButton,
  enable: Boolean
) {
  getActionButton(which).isEnabled = enable
}