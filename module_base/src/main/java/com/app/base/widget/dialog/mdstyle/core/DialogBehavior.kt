/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleRes
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.NEGATIVE
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.getActionButton
import com.app.base.widget.dialog.mdstyle.core.main.DialogLayout
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getWidthAndHeight
import com.app.base.widget.dialog.mdstyle.util.isVisibleForMd
import kotlin.math.min

interface DialogBehavior {
    /** 获取 dialog theme，根据不同主题模式(dark&light)，来使用不同的 style 风格 */
    @StyleRes fun getThemeRes(isDark: Boolean): Int

    /** 创建 dialog 的根布局 */
    fun createView(
      creatingContext: Context,
      dialogWindow: Window,
      layoutInflater: LayoutInflater,
      dialog: MaterialDialog
    ): ViewGroup

    /** 返回 [DialogLayout] 当你从 [createView] inflated 后. */
    fun getDialogLayout(root: ViewGroup): DialogLayout

    /** 设置窗口的宽度、高度约束 */
    fun setWindowConstraints(
      context: Context,
      window: Window,
      view: DialogLayout,
      @Px maxWidth: Int?
    )

    /** 设置 dialog 背景颜色. */
    fun setBackgroundColor(
      view: DialogLayout,
      @ColorInt color: Int,
      cornerRounding: Float
    )

    /** 在即将显示 dialog 前调用 */
    fun onPreShow(dialog: MaterialDialog)

    /** 在 dialog 显示后调用 */
    fun onPostShow(dialog: MaterialDialog)

    /** 在关闭对话框时调用，如果已经关闭则返回 true ，这里做一些释放资源的操作 */
    fun onDismiss(): Boolean
}

object ModalDialog : DialogBehavior {
    override fun getThemeRes(isDark: Boolean): Int {
        return if (isDark) {
            R.style.MD_Dark
        } else {
            R.style.MD_Light
        }
    }

    @SuppressLint("InflateParams")
    override fun createView(
      creatingContext: Context,
      dialogWindow: Window,
      layoutInflater: LayoutInflater,
      dialog: MaterialDialog
    ): ViewGroup {
        return layoutInflater.inflate(
          R.layout.md_dialog_base,
          null,
          false
        ) as ViewGroup
    }

    override fun getDialogLayout(root: ViewGroup): DialogLayout {
        return root as DialogLayout
    }

    override fun setWindowConstraints(
      context: Context,
      window: Window,
      view: DialogLayout,
      maxWidth: Int?
    ) {
        if (maxWidth == 0) {
            return
        }

        window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val wm = window.windowManager ?: return
        val res = context.resources
      val (windowWidth, windowHeight) = wm.getWidthAndHeight()
        val windowVerticalPadding =
            res.getDimensionPixelSize(R.dimen.md_dialog_vertical_margin)
        view.maxHeight = windowHeight - windowVerticalPadding * 2

        val lp = LayoutParams().apply {
            copyFrom(window.attributes)

            val windowHorizontalPadding =
                res.getDimensionPixelSize(R.dimen.md_dialog_horizontal_margin)
            val calculatedWidth = windowWidth - windowHorizontalPadding * 2
            val actualMaxWidth =
                maxWidth ?: res.getDimensionPixelSize(R.dimen.md_dialog_max_width)
            width = min(actualMaxWidth, calculatedWidth)
        }
        window.attributes = lp
    }

    override fun setBackgroundColor(
      view: DialogLayout,
      color: Int,
      cornerRounding: Float
    ) {
        view.background = GradientDrawable().apply {
            cornerRadius = cornerRounding
            setColor(color)
        }
    }

    override fun onPreShow(dialog: MaterialDialog) = Unit

    override fun onPostShow(dialog: MaterialDialog) {
        val negativeBtn = dialog.getActionButton(NEGATIVE)
        if (negativeBtn.isVisibleForMd()) {
            negativeBtn.post { negativeBtn.requestFocus() }
        }
        val positiveBtn = dialog.getActionButton(POSITIVE)
        if (positiveBtn.isVisibleForMd()) {
            positiveBtn.post { positiveBtn.requestFocus() }
        }
    }

    override fun onDismiss(): Boolean = false
}