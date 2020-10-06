package com.example.architecture.home.ui.model

import androidx.databinding.BaseObservable
import com.android.base.widget.adapter.item.ItemExpand
import com.android.base.widget.adapter.item.ItemHover
import com.android.base.widget.adapter.item.ItemPosition
import com.example.architecture.home.R
import com.example.architecture.home.ui.model.allgames.Model

class GroupModel: ItemExpand, ItemHover, ItemPosition, BaseObservable() {

    override var itemGroupPosition: Int = 0
    override var itemExpand: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }
    override var itemSublist: List<Any?>? = listOf(Model(""), Model(""), Model(""), Model(""))
    override var itemHover: Boolean = true
    override var itemPosition: Int = 0

    val title get() = "分组 [ $itemGroupPosition ]"

    val expandIcon get() = if (itemExpand) R.drawable.ic_arrow_expand else R.drawable.ic_arrow_collapse
}