package com.android.base.adapter.recycler

import android.view.View
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

@ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
open class KtViewHolder(override val containerView: View) : ViewHolder(containerView), LayoutContainer