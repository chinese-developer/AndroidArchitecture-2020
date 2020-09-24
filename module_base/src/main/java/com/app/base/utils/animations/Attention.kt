/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.CycleInterpolator


fun View.flash(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(ObjectAnimator.ofFloat(this, "alpha", 1f, 0f, 1f, 0f, 1f))
        this.duration = duration
    }
}

fun View.rubberBand(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.25f, 0.75f, 1.15f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.75f, 1.25f, 0.85f, 1f)
        )
        this.duration = duration
    }
}

fun View.shakeHorizontal(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        val animator = ObjectAnimator.ofFloat(this, "translationX", -10f, 10f)
        animator.interpolator = CycleInterpolator(5f)
        playTogether(animator)
        this.duration = duration
    }
}

fun View.shakeVertical(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        val animator = ObjectAnimator.ofFloat(this, "translationY", -10f, 10f)
        animator.interpolator = CycleInterpolator(5f)
        playTogether(animator)
        this.duration = duration
    }
}

fun View.swing(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
        )
        this.duration = duration
    }
}

fun View.tada(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().apply {
        playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
        )
        this.duration = duration
    }
}


