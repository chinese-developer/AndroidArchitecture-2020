package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

open class SlideScaleYItemAnimation: ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
            ObjectAnimator.ofFloat(
                view,
                "scaleY",
                1.07F,
                1f
            )
        )
    }
}
