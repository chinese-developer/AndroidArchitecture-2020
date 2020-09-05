/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.main

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.dimenPx
import com.app.base.widget.dialog.mdstyle.util.MDUtil.isRtl
import com.app.base.widget.dialog.mdstyle.util.isNotVisible
import com.app.base.widget.dialog.mdstyle.util.isVisibleForMd

@RestrictTo(LIBRARY_GROUP)
class DialogTitleLayout(
  context: Context,
  attrs: AttributeSet? = null
) : BaseSubLayout(context, attrs) {

  private val frameMarginVertical = dimenPx(R.dimen.md_dialog_frame_margin_vertical)
  private val frameMarginHorizontal = dimenPx(R.dimen.md_dialog_frame_margin_horizontal)
  private val titleMarginBottom = dimenPx(R.dimen.md_dialog_title_layout_margin_bottom)

  private val iconMargin = dimenPx(R.dimen.md_icon_margin)
  private val iconSize = dimenPx(R.dimen.md_icon_size)

  internal lateinit var iconView: ImageView
  internal lateinit var titleView: TextView

  fun shouldNotBeVisible(): Boolean = iconView.isNotVisible() && titleView.isNotVisible()

  override fun onFinishInflate() {
    super.onFinishInflate()
    iconView = findViewById(R.id.md_icon_title)
    titleView = findViewById(R.id.md_text_title)
  }

  /** 计算 icon + title + content measure */
  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    if (shouldNotBeVisible()) {
      setMeasuredDimension(0, 0)
      return
    }

    val parentWidth = getSize(widthMeasureSpec)
    var titleMaxWidth =
      parentWidth - (frameMarginHorizontal * 2)

    if (iconView.isVisibleForMd()) {
      iconView.measure(
          makeMeasureSpec(iconSize, MeasureSpec.EXACTLY),
          makeMeasureSpec(iconSize, MeasureSpec.EXACTLY)
      )
      titleMaxWidth -= (iconView.measuredWidth + iconMargin)
    }

    titleView.measure(
        makeMeasureSpec(titleMaxWidth, MeasureSpec.AT_MOST),
        makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )

    val iconViewHeight =
      if (iconView.isVisibleForMd()) iconView.measuredHeight else 0
    val requiredHeight = Math.max(
        iconViewHeight, titleView.measuredHeight
    )
    val actualHeight = requiredHeight + frameMarginVertical + titleMarginBottom

    setMeasuredDimension(
        parentWidth,
        actualHeight
    )
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    if (shouldNotBeVisible()) return

    val contentTop = frameMarginVertical
    val contentBottom = measuredHeight - titleMarginBottom
    val contentHeight = contentBottom - contentTop
    val contentMidPoint = contentBottom - (contentHeight / 2)

    val titleHalfHeight = titleView.measuredHeight / 2
    val titleTop = contentMidPoint - titleHalfHeight
    val titleBottom = contentMidPoint + titleHalfHeight
    var titleLeft: Int
    var titleRight: Int

    if (isRtl()) {
      titleRight = measuredWidth - frameMarginHorizontal
      titleLeft = titleRight - titleView.measuredWidth
    } else {
      titleLeft = frameMarginHorizontal
      titleRight = titleLeft + titleView.measuredWidth
    }

    if (iconView.isVisibleForMd()) {
      val iconHalfHeight = iconView.measuredHeight / 2

      val iconTop = contentMidPoint - iconHalfHeight
      val iconBottom = contentMidPoint + iconHalfHeight
      val iconLeft: Int
      val iconRight: Int

      if (isRtl()) {
        iconRight = titleRight
        iconLeft = iconRight - iconView.measuredWidth
        titleRight = iconLeft - iconMargin
        titleLeft = titleRight - titleView.measuredWidth
      } else {
        iconLeft = titleLeft
        iconRight = iconLeft + iconView.measuredWidth
        titleLeft = iconRight + iconMargin
        titleRight = titleLeft + titleView.measuredWidth
      }
      iconView.layout(iconLeft, iconTop, iconRight, iconBottom)
    }

    titleView.layout(titleLeft, titleTop, titleRight, titleBottom)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    if (drawDivider) {
      canvas.drawLine(
          0f,
          measuredHeight.toFloat() - dividerHeight.toFloat(),
          measuredWidth.toFloat(),
          measuredHeight.toFloat(),
          dividerPaint()
      )
    }
  }
}