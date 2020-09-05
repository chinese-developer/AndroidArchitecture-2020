/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForWidth

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-28 17:20
 *
 *      用来判断当前 message 内容是否超出显示区最大范围，如超出则设置 scrollView 滚动模式，
 *  并设置 [DialogLayout] 顶部与底部的分割线，给予用户视觉反馈效果，判断当前 message 是否触及顶部与底部。
 */
@RestrictTo(LIBRARY_GROUP)
class DialogScrollView(
  context: Context?,
  attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

  var rootView: DialogLayout? = null

  private val isScrollable: Boolean
    get() = getChildAt(0).measuredHeight > height

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    waitForWidth {
      invalidateDividers()
      invalidateOverScroll()
    }
  }

  override fun onScrollChanged(
    l: Int,
    t: Int,
    oldl: Int,
    oldt: Int
  ) {
    super.onScrollChanged(l, t, oldl, oldt)
    invalidateDividers()
  }

  private fun invalidateOverScroll() {
    overScrollMode = if (childCount == 0 || measuredHeight == 0 || !isScrollable) {
      View.OVER_SCROLL_NEVER
    } else {
      View.OVER_SCROLL_IF_CONTENT_SCROLLS
    }
  }

  fun invalidateDividers() {
    if (childCount == 0 && measuredHeight == 0 && !isScrollable) {
      rootView?.invalidateDividers(showTop = false, showBottom = false)
      return
    }
    val view = getChildAt(childCount - 1)
    val diff = view.bottom - (measuredHeight + scrollY)
    rootView?.invalidateDividers(
        scrollY > 0,
        diff > 0
    )
  }
}