/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

fun IntArray.appendAll(values: Collection<Int>): IntArray {
  return toMutableList()
      .apply { addAll(values) }
      .toIntArray()
}

fun IntArray.removeAll(values: Collection<Int>): IntArray {
  return toMutableList()
      .apply { removeAll { values.contains(it) } }
      .toIntArray()
}
