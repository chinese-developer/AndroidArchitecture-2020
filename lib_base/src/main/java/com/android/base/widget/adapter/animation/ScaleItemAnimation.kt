package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


class ScaleItemAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_SCALE_FROM) :
    ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
        return arrayOf(scaleX, scaleY)
    }

    companion object {

        private val DEFAULT_SCALE_FROM = .5f
    }
}
