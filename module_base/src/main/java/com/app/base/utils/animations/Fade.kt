/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("ObjectAnimatorBinding", "unused")

package com.app.base.utils.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View


fun View.fadeEnter(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        )
        it.duration = duration
    }
}

fun View.fadeExit(duration: Long = 500L): AnimatorSet {
    return AnimatorSet().also {
        it.playTogether(
                ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        )
        it.duration = duration
    }
}