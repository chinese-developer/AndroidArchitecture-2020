/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


/** 点击闪烁一下 alpha 由 gone-> visible */
fun View.fadeEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        )
        it.duration = duration
    }
}

/** 点击闪烁一下 alpha 由 visible-> gone(消失,点击空白处可再次触发点击事件) */
fun View.fadeExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        it.duration = duration
    }
}