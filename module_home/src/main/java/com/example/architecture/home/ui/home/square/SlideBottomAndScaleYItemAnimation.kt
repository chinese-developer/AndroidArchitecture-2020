package com.example.architecture.home.ui.home.square

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.android.base.widget.adapter.animation.ItemAnimation


class SlideBottomAndScaleYItemAnimation @JvmOverloads constructor(private val mDistance: Float = -1f) :
    ItemAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
            ObjectAnimator.ofFloat(
                view,
                "translationY",
                if (mDistance == -1f) view.measuredHeight.toFloat() else mDistance,
                0F,
                0f
            ),
            ObjectAnimator.ofFloat(
                view,
                "scaleY",
                1.08F,
                1f
            )
        )
    }
}
