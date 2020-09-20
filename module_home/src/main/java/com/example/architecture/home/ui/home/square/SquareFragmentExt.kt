package com.example.architecture.home.ui.home.square

import android.view.View
import android.view.View.OVER_SCROLL_NEVER
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.base.utils.android.views.getStringArray
import com.example.architecture.home.R
import com.example.architecture.home.ui.model.*
import com.example.architecture.home.ui.model.home.ItemTitle


fun View.adjustAndAverageHeight(position: Int, itemCount: Int, newHeight: Int) {
    with(layoutParams as RecyclerView.LayoutParams) {
        var top = 8
        var bottom = 8
        if (position == 0) {
            top = 16
        } else if (position == itemCount - 1) {
            bottom = 16
        }
        setMargins(32, top, 4, bottom)
        height = (newHeight - (itemCount * (top + bottom))) / itemCount
    }
}

fun fetchPrimaryItems(): MutableList<ItemTitle> {
    return mutableListOf<ItemTitle>().apply {
        secondaryItems().forEachIndexed { index, item ->
            add(ItemTitle(name = item.title, index == 0))
        }
    }
}

val titleArrays = getStringArray(R.array.group_title)
fun secondaryItems(): List<BaseModel> {
    return listOf(
        Model(titleArrays[0]),
        DoubleItemModel(titleArrays[1]),
        ThreeItemModel(titleArrays[2]),
        FourItemModel(titleArrays[3]),
        FiveItemModel(titleArrays[4]),
        SixItemModel(titleArrays[5])
    )
}

fun ViewPager2.setOverScrollModeNever() {
    (getChildAt(0) as? RecyclerView)?.overScrollMode = OVER_SCROLL_NEVER
}