/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.main

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.dimenPx
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColor

@RestrictTo(LIBRARY_GROUP)
abstract class BaseSubLayout internal constructor(
  context: Context,
  attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

  private val dividerPaint = Paint()
  protected val dividerHeight = dimenPx(R.dimen.md_divider_height)
  lateinit var dialog: MaterialDialog

  var drawDivider: Boolean = false
    set(value) {
      field = value
      invalidate()
    }

  init {
    @Suppress("LeakingThis")
    setWillNotDraw(false)
    dividerPaint.style = STROKE
    dividerPaint.strokeWidth = context.resources.getDimension(R.dimen.md_divider_height)
    dividerPaint.isAntiAlias = true
  }

  protected fun dividerPaint(): Paint {
    dividerPaint.color = getDividerColor()
    return dividerPaint
  }

  private fun getDividerColor(): Int =
    resolveColor(dialog.windowContext, attr = R.attr.md_divider_color)
}