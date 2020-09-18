package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

open class SlideInTopItemAnimation @JvmOverloads constructor(private val mDistance: Float = -1f) :
    ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
            ObjectAnimator.ofFloat(
                view,
                "translationY",
                if (mDistance == -1f) view.measuredHeight.toFloat() else -mDistance,
                0F,
                0f
            )
        )
    }
}
