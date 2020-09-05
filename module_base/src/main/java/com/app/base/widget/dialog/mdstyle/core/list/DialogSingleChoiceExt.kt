/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import androidx.annotation.ArrayRes
import androidx.annotation.CheckResult
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getStringArray

@CheckResult @JvmOverloads fun MaterialDialog.listItemsSingleChoice(
    items: List<CharSequence>? = null,
    @ArrayRes res: Int? = null,
    disabledIndices: IntArray? = null,
    initialSelection: Int = -1,
    waitForPositiveButton: Boolean = true,
    selection: SingleChoiceListener = null
): MaterialDialog {
    assertOneSet("listItemsSingleChoice", items, res)
    val array = items ?: windowContext.getStringArray(res).toList()
    require(initialSelection >= -1 || initialSelection < array.size) {
        "Initial selection $initialSelection must be between -1 and " +
                "the size of your items array ${array.size}"
    }

    if (getListAdapter() != null) {
        return updateListItemsSingleChoice(
            items = items,
            res = res,
            disabledIndices = disabledIndices,
            selection = selection
        )
    }

    setActionButtonEnabled(POSITIVE, initialSelection > -1)
    return customListAdapter(
        SingleChoiceDialogAdapter(
            dialog = this,
            items = array,
            disabledItems = disabledIndices,
            initialSelection = initialSelection,
            waitForActionButton = waitForPositiveButton,
            selection = selection
        )
    )
}

fun MaterialDialog.updateListItemsSingleChoice(
    items: List<CharSequence>? = null,
    @ArrayRes res: Int? = null,
    disabledIndices: IntArray? = null,
    selection: SingleChoiceListener = null
): MaterialDialog {
    assertOneSet("updateListItemsSingleChoice", items, res)
    val array = items ?: windowContext.getStringArray(res).toList()
    val adapter = getListAdapter()
    check(adapter is SingleChoiceDialogAdapter) {
        "updateListItemsSingleChoice(...) can't be used before you've created a single choice list dialog."
    }
    adapter.replaceItems(array, selection)
    disabledIndices?.let(adapter::disableItems)
    return this
}