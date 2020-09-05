/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

/** IT 数据源，SL 回调监听*/
interface DialogAdapter<in IT, in SL> {
    fun replaceItems(
      items: List<IT>,
      listener: SL? = null
    )

    fun disableItems(indices: IntArray)

    fun checkItems(indices: IntArray)

    fun uncheckItems(indices: IntArray)

    fun toggleItems(indices: IntArray)

    fun checkAllItems()

    fun uncheckAllItems()

    fun toggleAllChecked()

    fun isItemChecked(index: Int): Boolean

    fun getItemCount(): Int

    fun positiveButtonClicked()
}
