/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.util.DisplayMetrics
import android.view.View


fun View.bounceBottomEnter(duration: Long = 1000L): AnimatorSet {
    val dm: DisplayMetrics = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", 250 * dm.density, -30f, 10f, 0f)
        )
        this.duration = duration
    }
}

fun View.bounceEnter(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleX", 0.5f, 1.05f, 0.95f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1.05f, 0.95f, 1f)
        )
        this.duration = duration
    }
}

fun View.bounceLeftEnter(duration: Long = 1000L): AnimatorSet {
    val dm: DisplayMetrics = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", -250 * dm.density, 30f, -10f, 0f)
        )
        this.duration = duration
    }
}

fun View.bounceRightEnter(duration: Long = 1000L): AnimatorSet {
    val dm: DisplayMetrics = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "translationX", 250 * dm.density, -30f, 10f, 0f)
        )
        this.duration = duration
    }
}

fun View.bounceTopEnter(duration: Long = 1000L): AnimatorSet {
    val dm: DisplayMetrics = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "translationY", -250 * dm.density, 30f, -10f, 0f)
        )
        this.duration = duration
    }
}
