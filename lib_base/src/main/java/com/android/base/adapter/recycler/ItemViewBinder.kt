package com.android.base.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemViewBinder<T, VH : RecyclerView.ViewHolder> : com.drakeet.multitype.ItemViewBinder<T, VH>() {

    protected val dataManager: MultiTypeAdapter
        get() = adapter as MultiTypeAdapter

}

abstract class SimpleItemViewBinder<T> : ItemViewBinder<T, KtViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KtViewHolder {
        val layout = provideLayout(inflater, parent)
        val itemView = if (layout is Int) {
            inflater.inflate(layout, parent, false)
        } else
            layout as View
        return KtViewHolder(itemView).apply {
            onViewHolderCreated(this)
        }
    }

    protected open fun onViewHolderCreated(viewHolder: KtViewHolder) = Unit

    /**provide a layout id or a View*/
    abstract fun provideLayout(inflater: LayoutInflater, parent: ViewGroup): Any

}