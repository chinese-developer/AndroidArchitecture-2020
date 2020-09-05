/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.viewpagers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*
import androidx.viewpager.widget.ViewPager
import com.app.base.widget.dialog.mdstyle.util.MDUtil.ifNotZero

internal class WrapContentViewPager(
  context: Context,
  attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    override fun onMeasure(
      widthMeasureSpec: Int,
      heightMeasureSpec: Int
    ) {
        var newHeightSpec = heightMeasureSpec

        var maxChildHeight = 0
        forEachChild { child ->
            child.measure(
              widthMeasureSpec,
              makeMeasureSpec(0, UNSPECIFIED)
            )

            val h = child.measuredHeight
            if (h > maxChildHeight) {
                maxChildHeight = h
            }
        }

        val maxAllowedHeightFromParent = MeasureSpec.getSize(heightMeasureSpec)
        if (maxChildHeight > maxAllowedHeightFromParent) {
            maxChildHeight = maxAllowedHeightFromParent
        }
        maxChildHeight.ifNotZero {
            newHeightSpec = makeMeasureSpec(it, EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    private fun forEachChild(each: (View) -> Unit) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            each(child)
        }
    }
}
