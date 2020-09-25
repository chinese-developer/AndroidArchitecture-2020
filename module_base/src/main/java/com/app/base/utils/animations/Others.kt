/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


fun View.jelly(duration: Long = 700L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.3f, 0.5f, 0.9f, 0.8f, 0.9f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.3f, 0.5f, 0.9f, 0.8f, 0.9f, 1f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        it.duration = duration
    }
}

fun View.pagerEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 0.1f, 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 0.1f, 0.5f, 1f),
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 1080f, 720f, 360f, 0f)
        )
        it.duration = duration
    }
}

fun View.buttonPressedScaleXY(duration: Long = 200L): AnimatorSet {
    val animatorSet = AnimatorSet()
    val zoomOut = AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.975f),
                ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.975f),
        )
        it.duration = duration
    }
    val zoomIn = AnimatorSet().also {
        it.playTogether(
            ObjectAnimator.ofFloat(this, "scaleX", 0.975f, 1.0f),
            ObjectAnimator.ofFloat(this, "scaleY", 0.975f, 1.0f)
        )
        it.duration = duration
    }
    animatorSet.playSequentially(zoomOut, zoomIn)
    return animatorSet
}