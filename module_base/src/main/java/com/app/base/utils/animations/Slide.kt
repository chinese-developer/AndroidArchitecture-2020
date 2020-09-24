/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


fun View.slideBottomEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 0.5f, 0.9f, 0.8f, 0.9f, 1f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        this.duration = duration
    }
}

fun View.slideLeftEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationX", -250 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        this.duration = duration
    }
}

fun View.slideRightEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationX", 250 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        this.duration = duration
    }
}

fun View.slideTopEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationY", -250 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        this.duration = duration
    }
}

fun View.slideBottomExit(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationY", 0f, 250 * dm.density),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        this.duration = duration
    }
}

fun View.slideLeftExit(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationX", 0f, -250 * dm.density),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        this.duration = duration
    }
}

fun View.slideRightExit(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationX", 0f, 250 * dm.density),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        this.duration = duration
    }
}

fun View.slideTopExit(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "translationY", 0f, -250 * dm.density),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        this.duration = duration
    }
}
