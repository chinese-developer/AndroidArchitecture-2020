package com.app.base.widget.rv

import android.content.Context
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.android.views.dpToPx

class AdjustLinearLayoutManager(
    val line: Int = 0,
    context: Context,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        if (line > 0) {
            val newHeightSpec = View.MeasureSpec.makeMeasureSpec(50, EXACTLY)
            super.onMeasure(recycler, state, widthSpec, newHeightSpec)
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
        }
    }

    override fun canScrollVertically(): Boolean = false
}