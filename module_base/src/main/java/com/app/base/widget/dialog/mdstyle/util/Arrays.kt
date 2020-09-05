/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

inline fun <reified T> List<T>.pullIndices(indices: IntArray): List<T> {
    return mutableListOf<T>().apply {
        for (index in indices) {
            add(this@pullIndices[index])
        }
    }
}