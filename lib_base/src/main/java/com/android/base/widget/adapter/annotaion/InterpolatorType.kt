package com.android.base.widget.adapter.annotaion

import androidx.annotation.IntDef
import com.android.base.widget.adapter.BindingAdapter


@IntDef(*[BindingAdapter.LinearInterpolator, BindingAdapter.DecelerateInterpolator, BindingAdapter.AccelerateDecelerateInterpolator])
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class InterpolatorType