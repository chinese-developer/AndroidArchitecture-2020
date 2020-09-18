package com.android.base.widget.adapter.item

/**
 * 可粘性头部的条目
 */
interface ItemHover {
    /**
     * 是否启用粘性头部
     * [com.drake.brv.utils.RecyclerUtilsKt.linear]
     * [com.drake.brv.utils.RecyclerUtilsKt.grid]
     * [com.drake.brv.utils.RecyclerUtilsKt.staggered]
     */
    var itemHover: Boolean
}