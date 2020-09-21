package com.android.base.widget.adapter.item

/**
 * 可粘性头部的条目
 */
interface ItemHover {
    /**
     * 是否启用粘性头部
     * [com.android.base.widget.adapter.utils.RecyclerUtilsKt.linear]
     * [com.android.base.widget.adapter.utils.RecyclerUtilsKt.grid]
     * [com.android.base.widget.adapter.utils.RecyclerUtilsKt.staggered]
     */
    var itemHover: Boolean
}