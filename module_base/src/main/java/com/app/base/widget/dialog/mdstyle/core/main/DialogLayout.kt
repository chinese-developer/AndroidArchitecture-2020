
package com.app.base.widget.dialog.mdstyle.core.main

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.WindowManager
import android.widget.FrameLayout
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.LayoutMode
import com.app.base.widget.dialog.mdstyle.core.LayoutMode.WRAP_CONTENT
import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButtonLayout
import com.app.base.widget.dialog.mdstyle.core.button.shouldBeVisible
import com.app.base.widget.dialog.mdstyle.core.message.DialogContentLayout
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.dimenPx
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getWidthAndHeight

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-26 11:19
 *
 * The root layout of a dialog. Contains a [DialogTitleLayout], [DialogContentLayout],
 *  and [DialogActionButtonLayout].
 */
class DialogLayout(
  context: Context,
  attrs: AttributeSet?
) : FrameLayout(context, attrs) {

  var maxHeight: Int = 0
  var debugMode: Boolean = false
    set(value) {
      field = value
      setWillNotDraw(value)
    }

  internal val frameMarginVertical = dimenPx(R.dimen.md_dialog_frame_margin_vertical)
  internal val frameMarginVerticalLess = dimenPx(R.dimen.md_dialog_frame_margin_vertical_less)

  lateinit var dialog: MaterialDialog
  lateinit var titleLayout: DialogTitleLayout
  lateinit var contentLayout: DialogContentLayout
  var buttonsLayout: DialogActionButtonLayout? = null
  var layoutMode: LayoutMode = WRAP_CONTENT

  private var isButtonsLayoutAChild: Boolean = true
  private var windowHeight: Int = -1

  /** after setContentView and before onMeasure **/
  override fun onFinishInflate() {
    super.onFinishInflate()
    titleLayout = findViewById(R.id.md_title_layout)
    contentLayout = findViewById(R.id.md_content_layout)
    buttonsLayout = findViewById(R.id.md_button_layout)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    val (_, windowHeight) = windowManager.getWidthAndHeight()
    this.windowHeight = windowHeight
  }

  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    val specWidth = getSize(widthMeasureSpec)
    var specHeight = getSize(heightMeasureSpec)
    if (maxHeight in 1 until specHeight) {
      specHeight = maxHeight
    }

    titleLayout.measure(
        makeMeasureSpec(specWidth, EXACTLY),
        makeMeasureSpec(0, UNSPECIFIED)
    )
    if (buttonsLayout.shouldBeVisible()) {
      buttonsLayout!!.measure(
          makeMeasureSpec(specWidth, EXACTLY),
          makeMeasureSpec(0, UNSPECIFIED)
      )
    }

    val titleAndButtonsHeight =
      titleLayout.measuredHeight + (buttonsLayout?.measuredHeight ?: 0)
    val remainingHeight = specHeight - titleAndButtonsHeight
    contentLayout.measure(
        makeMeasureSpec(specWidth, EXACTLY),
        makeMeasureSpec(remainingHeight, AT_MOST)
    )

    if (layoutMode == WRAP_CONTENT) {
      val totalHeight = titleLayout.measuredHeight +
          contentLayout.measuredHeight +
          (buttonsLayout?.measuredHeight ?: 0)
      setMeasuredDimension(specWidth, totalHeight)
    } else {
      setMeasuredDimension(specWidth, windowHeight)
    }
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    val titleLeft = 0
    val titleTop = 0
    val titleRight = measuredWidth
    val titleBottom = titleLayout.measuredHeight
    titleLayout.layout(
        titleLeft,
        titleTop,
        titleRight,
        titleBottom
    )

    val buttonsTop: Int
    if (isButtonsLayoutAChild) {
      buttonsTop =
        measuredHeight - (buttonsLayout?.measuredHeight ?: 0)
      if (buttonsLayout.shouldBeVisible()) {
        val buttonsLeft = 0
        val buttonsRight = measuredWidth
        val buttonsBottom = measuredHeight
        buttonsLayout!!.layout(
            buttonsLeft,
            buttonsTop,
            buttonsRight,
            buttonsBottom
        )
      }
    } else {
      buttonsTop = measuredHeight
    }

    val contentLeft = 0
    val contentRight = measuredWidth
    contentLayout.layout(
        contentLeft,
        titleBottom,
        contentRight,
        buttonsTop
    )
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    if (!debugMode) return

    // np
  }

  fun attachDialog(dialog: MaterialDialog) {
    titleLayout.dialog = dialog
    buttonsLayout?.dialog = dialog
  }

  fun attachButtonsLayout(buttonsLayout: DialogActionButtonLayout) {
    this.buttonsLayout = buttonsLayout
    this.isButtonsLayoutAChild = false
  }

  /**
   * 显示与隐藏 顶部标题与底部 buttons 分割线
   */
  fun invalidateDividers(
    showTop: Boolean,
    showBottom: Boolean
  ) {
    titleLayout.drawDivider = showTop
    buttonsLayout?.drawDivider = showBottom
  }
}

