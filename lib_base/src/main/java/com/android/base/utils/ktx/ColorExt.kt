package com.android.base.utils.ktx

import android.graphics.Color

fun getBlackWhiteColor(color: Int): Int {
    val darkness =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
    return if (darkness >= 0.5) {
        Color.WHITE
    } else Color.BLACK
}

fun getBlackWhiteColorReverser(color: Int): Int {
    val darkness =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
    return if (darkness >= 0.5) {
        Color.BLACK
    } else Color.WHITE
}