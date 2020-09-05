/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import androidx.annotation.ArrayRes
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getStringArray

/**
 * 多选框列表
 *
 * @param res 字符串数组资源
 * @param items 字符串数组
 * @param disableIndices 禁止勾选的列表项
 * @param initialSelection 默认勾选的列表项
 * @param waitForPositiveButton 设置为 true 时，[selection] 监听并不会执行回调，知道 positiveButton 被按下
 * @param allowEmptySelection 设置为 true 时，列表项允许没有被勾选。否则，必须勾选。
 * @param selection 列表项中每一个 item 对应一个监听器
 */
@JvmOverloads
@CheckResult fun MaterialDialog.listItemsMultiChoice(
    @ArrayRes res: Int? = null,
    items: List<CharSequence>? = null,
    disableIndices: IntArray? = null,
    initialSelection: IntArray = IntArray(0),
    waitForPositiveButton: Boolean = true,
    allowEmptySelection: Boolean = false,
    selection: MultiChoiceListener = null
): MaterialDialog {
    assertOneSet("listItemsMultiChoice", items, res)
    val array = items ?: windowContext.getStringArray(res).toList()

    if (getListAdapter() != null) {
        return updateListItemsMultiChoice(
            res = res,
            items = array,
            disabledIndices = disableIndices,
            selection = selection
        )
    }

    setActionButtonEnabled(POSITIVE, allowEmptySelection || initialSelection.isNotEmpty())
    return customListAdapter(
        MultiChoiceDialogAdapter(
            dialog = this,
            items = array,
            disabledItems = disableIndices,
            initialSelection = initialSelection,
            waitForActionButton = waitForPositiveButton,
            allowEmptySelection = allowEmptySelection,
            selection = selection
        )
    )
}

fun MaterialDialog.updateListItemsMultiChoice(
    @ArrayRes res: Int? = null,
    items: List<CharSequence>? = null,
    disabledIndices: IntArray? = null,
    selection: MultiChoiceListener = null
): MaterialDialog {
    assertOneSet("updateListItemsMultiChoice", items, res)
    val array = items ?: windowContext.getStringArray(res).toList()
    val adapter = getListAdapter()
    check(adapter is MultiChoiceDialogAdapter) {
        "updateListItemsMultiChoice(...) can't be used before you've created a multiple choice list dialog."
    }
    adapter.replaceItems(array, selection)
    disabledIndices?.let(adapter::disableItems)
    return this
}

fun MaterialDialog.customListAdapter(
    adapter: RecyclerView.Adapter<*>,
    layoutManager: LayoutManager? = null
): MaterialDialog {
    this.view.contentLayout.addRecyclerView(
        dialog = this,
        adapter = adapter,
        layoutManager = layoutManager
    )
    return this
}