/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.callbacks

import com.app.base.widget.dialog.mdstyle.DialogCallback
import com.app.base.widget.dialog.mdstyle.MaterialDialog

internal fun MutableList<DialogCallback>.invokeAll(dialog: MaterialDialog) {
  for (callback in this) {
    callback.invoke(dialog)
  }
}

/**
 * 当对话框 [MaterialDialog.show] 前会触发该回调监听，并不会覆盖其他监听。
 */
fun MaterialDialog.onPreShow(callback: DialogCallback): MaterialDialog {
  this.preShowListeners.add(callback)
  return this
}

/**
 * 当对话框 [MaterialDialog.show] 时调用该回调监听，并不会覆盖其他监听。
 *
 * 如果当前对话框处于显示状态，那么会立即执行该回调。
 */
fun MaterialDialog.onShow(callback: DialogCallback): MaterialDialog {
  this.showListeners.add(callback)
  if (this.isShowing) {
    this.showListeners.invokeAll(this)
  }
  setOnShowListener { showListeners.invokeAll(this) }
  return this
}

/**
 * 当对话框 [MaterialDialog.cancel] 时调用该回调监听，并不会覆盖其他监听。
 */
fun MaterialDialog.onCancel(callback: DialogCallback): MaterialDialog {
  this.cancelListeners.add(callback)
  setOnCancelListener { cancelListeners.invokeAll(this) }
  return this
}

/**
 * 当对话框 [MaterialDialog.dismiss] 时调用该回调监听，并不会覆盖其他监听。
 */
fun MaterialDialog.onDismiss(callback: DialogCallback): MaterialDialog {
  this.dismissListeners.add(callback)
  setOnDismissListener { dismissListeners.invokeAll(this) }
  return this
}