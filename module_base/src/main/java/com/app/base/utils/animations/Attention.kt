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

/** 相机闪烁 */
fun View.flash(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(ObjectAnimator.ofFloat(this, "alpha", 1f, 0f, 1f, 0f, 1f))
        it.duration = duration
    }
}

/** 史莱姆果冻 */
fun View.rubberBand(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.25f, 0.75f, 1.15f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.75f, 1.25f, 0.85f, 1f)
        )
        it.duration = duration
    }
}

/** 左右多次晃动 */
fun View.shakeHorizontal(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        val animator = ObjectAnimator.ofFloat(this, "translationX", -10f, 10f)
        animator.interpolator = CycleInterpolator(5f)
        it.playTogether(animator)
        it.duration = duration
    }
}

/** 上下多次晃动 */
fun View.shakeVertical(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        val animator = ObjectAnimator.ofFloat(this, "translationY", -10f, 10f)
        animator.interpolator = CycleInterpolator(5f)
        it.playTogether(animator)
        it.duration = duration
    }
}

/** 右侧倾斜一下后左右多次晃动 */
fun View.swing(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
        )
        it.duration = duration
    }
}

/** 缩小放大中多次晃动 */
fun View.tada(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
        )
        it.duration = duration
    }
}


