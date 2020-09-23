package com.app.base.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

object AnimatorExt {

    /**
     * 先缩小在放大动画
     */
    fun View.playSequentiallyWithZoomOutAndInAnim() {
        AnimatorSet().apply { playSequentially(zoomOutAnim(), zoomInAnim()) }.start()
    }

    /** 缩放动画 */
    fun View.zoomOutAnim(duration: Long = 200): AnimatorSet {
        val scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.98f)
        val scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.98f)
        return AnimatorSet().apply {
            playTogether(scaleXAnim, scaleYAnim)
            interpolator = LinearInterpolator()
            this.duration = duration
        }
    }

    /** 放大动画 */
    fun View.zoomInAnim(duration: Long = 200): AnimatorSet {
        val scaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 0.98f, 1.0f)
        val scaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 0.98f, 1.0f)
        return AnimatorSet().apply {
            playTogether(scaleXAnim, scaleYAnim)
            interpolator = LinearInterpolator()
            this.duration = duration
        }
    }

}