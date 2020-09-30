/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


/** 从底部平移上方200dp==,像抓麻将牌 */
fun View.flipBottomEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationX", -90f, 0f),
                ObjectAnimator.ofFloat(this, "translationY", 200 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        it.duration = duration
    }
}

fun View.flipHorizontalEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f))
        it.duration = duration
    }
}

fun View.flipHorizontalSwingEnter(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationY", 90f, -10f, 10f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.25f, 0.5f, 0.75f, 1f)
        )
        it.duration = duration
    }
}

fun View.flipLeftEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationY", 90f, 0f),
                ObjectAnimator.ofFloat(this, "translationX", -200 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        it.duration = duration
    }
}

fun View.flipRightEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f),
                ObjectAnimator.ofFloat(this, "translationX", 200 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        it.duration = duration
    }
}

fun View.flipTopEnter(duration: Long = 500L): AnimatorSet {
    val dm = context.resources.displayMetrics
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationX", -90f, 0f),
                ObjectAnimator.ofFloat(this, "translationY", -200 * dm.density, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.2f, 1f)
        )
        it.duration = duration
    }
}

/** 像上下两端, 只按压上端后, 下端翘起, 上端下压的效果 */
fun View.flipVerticalEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(ObjectAnimator.ofFloat(this, "rotationX", 90f, 0f))
        it.duration = duration
    }
}

/** 像上下两端, 只按压上端后, 下端翘起, 上端下压的效果. 最终加bounce抖动 */
fun View.flipVerticalSwingEnter(duration: Long = 1000L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationX", 90f, -10f, 10f, 0f),
                ObjectAnimator.ofFloat(this, "alpha", 0.25f, 0.5f, 0.75f, 1f)
        )
        it.duration = duration
    }
}

/** 像左右两端, 只按压右端后的效果, 最终 view 彻底消失, 无法在触碰 */
fun View.flipHorizontalExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        it.duration = duration
    }
}

fun View.flipVerticalExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "rotationX", 0f, 90f),
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        it.duration = duration
    }
}