package com.android.base.widget.adapter.annotaion

import androidx.annotation.IntDef
import com.android.base.widget.adapter.BindingAdapter


@IntDef(*[BindingAdapter.ALPHA, BindingAdapter.SCALE, BindingAdapter.SLIDE_BOTTOM, BindingAdapter.SLIDE_TOP, BindingAdapter.SLIDE_LEFT, BindingAdapter.SLIDE_RIGHT])
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class AnimationType