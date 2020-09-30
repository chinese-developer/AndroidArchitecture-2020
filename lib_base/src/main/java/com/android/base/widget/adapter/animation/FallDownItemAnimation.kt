package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


class FallDownItemAnimation : ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val translationY = ObjectAnimator.ofFloat(view, "translationY", 40f, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.01f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.01f, 1f)

        return arrayOf(translationY, scaleX, scaleY)
    }
}
