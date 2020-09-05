/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.list

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForWidth
import com.app.base.widget.dialog.mdstyle.util.invalidateDividers

typealias InvalidateDividersDelegate = (scrolledDown: Boolean, atBottom: Boolean) -> Unit

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-28 18:32
 *
 *  当 [RecyclerView] 不可滚动时，设置分割线是无意义的
 */
@RestrictTo(LIBRARY_GROUP)
class DialogRecyclerView(
  context: Context,
  attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var invalidateDividersDelegate: InvalidateDividersDelegate? = null

    private val scrollListeners = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(
          recyclerView: RecyclerView,
          dx: Int,
          dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            invalidateDividers()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        waitForWidth {
            invalidateDividers()
            invalidateOverScroll()
        }
        addOnScrollListener(scrollListeners)
    }

    override fun onDetachedFromWindow() {
        removeOnScrollListener(scrollListeners)
        super.onDetachedFromWindow()
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

    fun attach(dialog: MaterialDialog) {
        this.invalidateDividersDelegate = dialog::invalidateDividers
    }

    private fun invalidateOverScroll() {
        overScrollMode = when {
            childCount == 0 || measuredHeight == 0 -> OVER_SCROLL_NEVER
            isScrollable() -> OVER_SCROLL_NEVER
            else -> OVER_SCROLL_IF_CONTENT_SCROLLS
        }
    }

    fun invalidateDividers() {
        if (childCount == 0 || measuredHeight == 0) return
        invalidateDividersDelegate?.invoke(!isAtTop(), !isAtBottom())
    }

    private fun isAtTop(): Boolean {
        return when (val lm = layoutManager) {
          is LinearLayoutManager -> lm.findFirstCompletelyVisibleItemPosition() == 0
          is GridLayoutManager -> lm.findFirstCompletelyVisibleItemPosition() == 0
            else -> false
        }
    }

    private fun isAtBottom(): Boolean {
        val lastIndex = adapter!!.itemCount - 1
        return when (val lm = layoutManager) {
          is LinearLayoutManager -> lm.findLastCompletelyVisibleItemPosition() == lastIndex
          is GridLayoutManager -> lm.findLastCompletelyVisibleItemPosition() == lastIndex
            else -> false
        }
    }

    private fun isScrollable(): Boolean = isAtBottom() && isAtTop()
}