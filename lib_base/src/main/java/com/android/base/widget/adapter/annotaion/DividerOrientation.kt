package com.android.base.widget.adapter.annotaion

import androidx.annotation.IntDef
import com.android.base.widget.adapter.BindingAdapter

@IntDef(*[BindingAdapter.VERTICAL, BindingAdapter.HORIZONTAL, BindingAdapter.GRID])
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class DividerOrientation