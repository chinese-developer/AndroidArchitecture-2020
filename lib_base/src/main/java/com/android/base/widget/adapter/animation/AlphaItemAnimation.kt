package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


class AlphaItemAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_ALPHA_FROM) :
    ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f))
    }

    companion object {

        private val DEFAULT_ALPHA_FROM = 0f
    }

}
