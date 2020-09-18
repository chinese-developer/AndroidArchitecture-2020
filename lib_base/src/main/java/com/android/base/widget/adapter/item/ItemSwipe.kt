package com.android.base.widget.adapter.item

import com.android.base.widget.adapter.annotaion.ItemOrientation

/**
 * 可侧滑的条目
 */
interface ItemSwipe {

    /**
     * 侧滑方向
     * @see ItemOrientation
     */
    var itemOrientationSwipe: Int
}