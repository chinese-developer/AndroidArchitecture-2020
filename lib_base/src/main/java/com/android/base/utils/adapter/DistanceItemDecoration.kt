package com.android.base.utils.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.android.UnitConverter.dpToPx

class DistanceItemDecoration : RecyclerView.ItemDecoration() {

    private val verticalMargin = dpToPx(4)
    private val firstItemMargin = dpToPx(4)
    private val lastItemMargin = dpToPx(8)
    private val leftMargin = dpToPx(14)

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        outRect.left = leftMargin
        outRect.right = verticalMargin
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.top = firstItemMargin
                outRect.bottom = verticalMargin
            }
            state.itemCount - 1 -> {
                outRect.top = verticalMargin
                outRect.bottom = lastItemMargin
            }
            else -> {
                outRect.top = verticalMargin
                outRect.bottom = verticalMargin
            }
        }
    }
}