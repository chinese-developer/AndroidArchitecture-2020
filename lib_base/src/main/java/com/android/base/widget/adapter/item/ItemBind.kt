package com.android.base.widget.adapter.item

import com.android.base.widget.adapter.BindingAdapter

/**
 * 推荐使用DataBinding来进行数据绑定[com.android.sdk.BindingAdapter.modelId], 或者函数[com.android.base.widget.adapter.BindingAdapter.onBind]
 * 该接口进行UI操作不符合MVVM架构, 因为Model中不允许出现View引用
 */
interface ItemBind {
    fun onBind(holder: BindingAdapter.BindingViewHolder)
}