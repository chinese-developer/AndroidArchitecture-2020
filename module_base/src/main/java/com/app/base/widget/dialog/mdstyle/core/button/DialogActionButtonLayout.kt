/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.button

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.appcompat.widget.AppCompatCheckBox
import com.app.base.widget.dialog.mdstyle.core.WhichButton
import com.app.base.widget.dialog.mdstyle.core.main.BaseSubLayout
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.dimenPx
import com.app.base.widget.dialog.mdstyle.util.MDUtil.isRtl
import com.app.base.widget.dialog.mdstyle.util.isVisibleForMd

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-26 15:46
 *
 *  管理一组三个按钮 [DialogActionButton] (measuring,layout, etc.).
 *  并处理他们显示与隐藏时，位置的切换。
 *  还处理了可选的复选框提示。
 *
 */
@RestrictTo(LIBRARY_GROUP)
class DialogActionButtonLayout(
  context: Context,
  attrs: AttributeSet? = null
) : BaseSubLayout(context, attrs) {

  companion object {
    const val INDEX_POSITIVE = 0
    const val INDEX_NEGATIVE = 1
    const val INDEX_NEUTRAL = 2
  }

  internal var stackButtons: Boolean = false
  private val buttonFramePadding = dimenPx(R.dimen.md_action_button_frame_padding) -
      dimenPx(R.dimen.md_action_button_inset_horizontal)
  private val buttonFramePaddingNeutral = dimenPx(R.dimen.md_action_button_frame_padding_neutral)
  private val buttonFrameSpecHeight = dimenPx(R.dimen.md_action_button_frame_spec_height)

  private val checkBoxPromptMarginVertical = dimenPx(R.dimen.md_checkbox_prompt_margin_vertical)
  private val checkBoxPromptMarginHorizontal = dimenPx(R.dimen.md_checkbox_prompt_margin_horizontal)

  lateinit var checkBoxPrompt: AppCompatCheckBox
  lateinit var actionButtons: Array<DialogActionButton>
  val visibleButtons: Array<DialogActionButton>
    get() = actionButtons.filter { it.isVisibleForMd() }
        .toTypedArray()

  override fun onFinishInflate() {
    super.onFinishInflate()
    actionButtons = arrayOf(
        findViewById(R.id.md_button_positive),
        findViewById(R.id.md_button_negative),
        findViewById(R.id.md_button_neutral)
    )
    checkBoxPrompt = findViewById(R.id.md_checkbox_prompt)

    for ((i, btn) in actionButtons.withIndex()) {
      val which = WhichButton.fromIndex(i)
      btn.setOnClickListener { dialog.onActionButtonClicked(which) }
    }
  }

  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    if (!shouldBeVisible()) {
      setMeasuredDimension(0, 0)
      return
    }

    val parentWidth = getSize(widthMeasureSpec)

    if (checkBoxPrompt.isVisibleForMd()) {
      val checkboxPromptMaxWidth = parentWidth - (checkBoxPromptMarginHorizontal * 2)
      checkBoxPrompt.measure(
          makeMeasureSpec(checkboxPromptMaxWidth, AT_MOST),
          makeMeasureSpec(0, UNSPECIFIED)
      )
    }

    val baseContext = dialog.context
    val appContext = dialog.windowContext
    for (button in visibleButtons) {
      button.update(
          baseContext = baseContext,
          appContext = appContext,
          stacked = stackButtons,
          id = button.id
      )
      if (stackButtons) {
        button.measure(
            makeMeasureSpec(parentWidth, EXACTLY),
            makeMeasureSpec(buttonFrameSpecHeight, EXACTLY)
        )
      } else {
        button.measure(
            makeMeasureSpec(0, UNSPECIFIED),
            makeMeasureSpec(buttonFrameSpecHeight, EXACTLY)
        )
      }
    }

    if (visibleButtons.isNotEmpty() && !stackButtons) {
      var totalWidth = 0
      for (button in visibleButtons) {
        totalWidth += button.measuredWidth
      }
      if (totalWidth >= parentWidth && !stackButtons) {
        stackButtons = true
        for (button in visibleButtons) {
          button.update(
              baseContext = baseContext,
              appContext = appContext,
              stacked = true,
              id = button.id
          )
          button.measure(
              makeMeasureSpec(parentWidth, EXACTLY),
              makeMeasureSpec(buttonFrameSpecHeight, EXACTLY)
          )
        }
      }
    }

    var totalHeight = requiredHeightForButtons()
    if (checkBoxPrompt.isVisibleForMd()) {
      totalHeight += checkBoxPrompt.measuredHeight + (checkBoxPromptMarginVertical * 2)
    }

    setMeasuredDimension(parentWidth, totalHeight)
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    if (!shouldBeVisible()) return

    if (checkBoxPrompt.isVisibleForMd()) {
      val promptLeft: Int
      val promptTop: Int
      val promptRight: Int
      val promptBottom: Int

      if (isRtl()) {
        promptRight = measuredWidth - checkBoxPromptMarginHorizontal
        promptLeft = promptRight - checkBoxPrompt.measuredWidth
        promptTop = checkBoxPromptMarginVertical
        promptBottom = promptTop + checkBoxPrompt.measuredHeight
      } else {
        promptLeft = checkBoxPromptMarginHorizontal
        promptRight = promptLeft + checkBoxPrompt.measuredWidth
        promptTop = checkBoxPromptMarginVertical
        promptBottom = promptTop + checkBoxPrompt.measuredHeight
      }

      checkBoxPrompt.layout(
          promptLeft,
          promptTop,
          promptRight,
          promptBottom
      )
    }

    if (stackButtons) {
      val leftX = buttonFramePadding
      val rightX = measuredWidth - buttonFramePadding
      var bottomY = measuredHeight
      for (button in visibleButtons.reversed()) {
        val topY = bottomY - buttonFrameSpecHeight
        button.layout(leftX, topY, rightX, bottomY)
        bottomY = topY
      }
    } else {
      val topY = measuredHeight - buttonFrameSpecHeight
      val bottomY = measuredHeight

      if (isRtl()) {
        if (actionButtons[INDEX_NEUTRAL].isVisibleForMd()) {
          val btn = actionButtons[INDEX_NEUTRAL]
          val rightX = measuredWidth - buttonFramePaddingNeutral
          btn.layout(
              rightX - btn.measuredWidth,
              topY,
              rightX,
              bottomY
          )
        }

        var leftX = buttonFramePadding
        if (actionButtons[INDEX_POSITIVE].isVisibleForMd()) {
          val btn = actionButtons[INDEX_POSITIVE]
          val rightX = leftX + btn.measuredWidth
          btn.layout(leftX, topY, rightX, bottomY)
          leftX = rightX
        }

        if (actionButtons[INDEX_NEGATIVE].isVisibleForMd()) {
          val btn = actionButtons[INDEX_NEGATIVE]
          val rightX = leftX + btn.measuredWidth
          btn.layout(leftX, topY, rightX, bottomY)
        }
      } else {
        if (actionButtons[INDEX_NEUTRAL].isVisibleForMd()) {
          val btn = actionButtons[INDEX_NEUTRAL]
          val leftX = buttonFramePaddingNeutral
          btn.layout(
              leftX,
              topY,
              leftX + btn.measuredWidth,
              bottomY
          )
        }

        var rightX = measuredWidth - buttonFramePadding
        if (actionButtons[INDEX_POSITIVE].isVisibleForMd()) {
          val btn = actionButtons[INDEX_POSITIVE]
          val leftX = rightX - btn.measuredWidth
          btn.layout(leftX, topY, rightX, bottomY)
          rightX = leftX
        }

        if (actionButtons[INDEX_NEGATIVE].isVisibleForMd()) {
          val btn = actionButtons[INDEX_NEGATIVE]
          val leftX = rightX - btn.measuredWidth
          btn.layout(leftX, topY, rightX, bottomY)
        }
      }
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    if (drawDivider) {
      canvas.drawLine(
          0f,
          0f,
          measuredWidth.toFloat(),
          dividerHeight.toFloat(),
          dividerPaint()
      )
    }
  }

  private fun requiredHeightForButtons(): Int = when {
    visibleButtons.isEmpty() -> 0
    stackButtons -> visibleButtons.size * buttonFrameSpecHeight
    else -> buttonFrameSpecHeight
  }
}

@RestrictTo(LIBRARY_GROUP)
fun DialogActionButtonLayout?.shouldBeVisible(): Boolean {
  if (this == null) {
    return false
  }
  return visibleButtons.isNotEmpty() || checkBoxPrompt.isVisibleForMd()
}