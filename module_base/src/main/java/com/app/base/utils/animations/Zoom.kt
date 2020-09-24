/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

fun View.zoomInBottomEnter(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", h, -60f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        this.duration = duration
    }
}

fun View.zoomInEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        )
        this.duration = duration
    }
}

fun View.zoomInLeftEnter(duration: Long = 500L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", -w, 48f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        this.duration = duration
    }
}

fun View.zoomInRightEnter(duration: Long = 750L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", w, -48f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        this.duration = duration
    }
}

fun View.zoomInTopEnter(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", -h, 60f, 0f)
        )
        this.duration = duration
    }
}

fun View.zoomInExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, -60f, h)
        )
        this.duration = duration
    }
}

fun View.zoomOutExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.3f, 0f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.3f, 0f)
        )
        this.duration = duration
    }
}

fun View.zoomOutBottomExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, -60f, h)
        )
        this.duration = duration
    }
}

fun View.zoomOutLeftExit(duration: Long = 1000L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, 42f, -w)
        )
        this.duration = duration
    }
}

fun View.zoomOutRightExit(duration: Long = 1000L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationX", 0f, -42f, w),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
        )
        this.duration = duration
    }
}

fun View.zoomOutTopExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, 60f, -h),
        )
        this.duration = duration
    }
}