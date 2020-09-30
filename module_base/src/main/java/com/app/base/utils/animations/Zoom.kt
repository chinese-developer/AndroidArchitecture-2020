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
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", h, -60f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        it.duration = duration
    }
}

/** 缩放, 由小到大 with alpha */
fun View.zoomInEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        )
        it.duration = duration
    }
}

fun View.zoomInLeftEnter(duration: Long = 500L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", -w, 48f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        it.duration = duration
    }
}

fun View.zoomInRightEnter(duration: Long = 750L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", w, -48f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f)
        )
        it.duration = duration
    }
}

fun View.zoomInTopEnter(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.475f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", -h, 60f, 0f)
        )
        it.duration = duration
    }
}

/** 缩放由大到小, 然后右下方向抛物线彻底消失 */
fun View.zoomInExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, -60f, h)
        )
        it.duration = duration
    }
}

/** 缩放由大到小, 消失(空白处可再次被点击事件触发) */
fun View.zoomOutExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.3f, 0f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.3f, 0f)
        )
        it.duration = duration
    }
}

/** 缩放由大到小, 然后 translationY 下方消失 */
fun View.zoomOutBottomExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, -60f, h)
        )
        it.duration = duration
    }
}

/** 缩放由大到小, 然后上方平移消失 */
fun View.zoomOutLeftExit(duration: Long = 1000L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, 42f, -w)
        )
        it.duration = duration
    }
}

fun View.zoomOutRightExit(duration: Long = 1000L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val w = measuredWidth.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationX", 0f, -42f, w),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
        )
        it.duration = duration
    }
}

fun View.zoomOutTopExit(duration: Long = 600L): AnimatorSet {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val h = measuredHeight.toFloat()
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 0f),
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.475f, 0.1f),
                ObjectAnimator.ofFloat(this, "translationY", 0f, 60f, -h),
        )
        it.duration = duration
    }
}