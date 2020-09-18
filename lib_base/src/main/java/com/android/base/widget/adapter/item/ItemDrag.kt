package com.android.base.widget.adapter.item

import com.android.base.widget.adapter.annotaion.ItemOrientation

/**
 * 可拖拽
 */
interface ItemDrag {
    /**
     * 拖拽方向
     * @see ItemOrientation
     */
    var itemOrientationDrag: Int
}