package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


class SlideLeftItemAnimation : ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
            ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f)
        )
    }
}
