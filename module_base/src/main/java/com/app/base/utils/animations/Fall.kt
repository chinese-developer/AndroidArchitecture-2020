/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/** 放大 -> 缩小 with alpha */
fun View.allEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 2f, 1.5f, 1f).setDuration(duration),
                ObjectAnimator.ofFloat(this, "scaleY", 2f, 1.5f, 1f).setDuration(duration),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(duration)
        )
        it.duration = duration
    }
}

/** 放大 -> 缩小 with rotate */
fun View.fallRotateEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 2f, 1.5f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 2f, 1.5f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 45f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        )
        it.duration = duration
    }
}