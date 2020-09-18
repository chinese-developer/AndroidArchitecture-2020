package com.android.base.widget.adapter.listener

import androidx.recyclerview.widget.RecyclerView
import com.android.base.widget.adapter.BindingAdapter

interface OnBindViewHolderListener {
    fun onBindViewHolder(rv: RecyclerView,
                         adapter: BindingAdapter,
                         holder: BindingAdapter.BindingViewHolder,
                         position: Int)
}