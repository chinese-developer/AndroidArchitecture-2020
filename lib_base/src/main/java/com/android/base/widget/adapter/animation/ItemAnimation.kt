package com.android.base.widget.adapter.animation

import android.animation.Animator
import android.view.View


interface ItemAnimation {

    /**
     * 处理item被添加的时候的进入动画
     */
    fun getAnimators(view: View): Array<Animator>
}
