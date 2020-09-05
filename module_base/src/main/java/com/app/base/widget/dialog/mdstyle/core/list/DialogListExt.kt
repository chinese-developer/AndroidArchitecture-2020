/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import androidx.annotation.ArrayRes
import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getStringArray
import com.app.base.widget.dialog.mdstyle.util.MDUtil.ifNotZero
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveDrawable
import com.app.base.widget.dialog.mdstyle.util.adjustAlpha
import com.app.base.widget.dialog.mdstyle.util.resolveColor

@CheckResult fun MaterialDialog.getRecyclerView(): RecyclerView {
  return this.view.contentLayout.recyclerView ?: throw IllegalStateException(
      "This dialog is not a list dialog."
  )
}

/** A shortcut to [RecyclerView.getAdapter] on [getRecyclerView]. */
@CheckResult fun MaterialDialog.getListAdapter(): RecyclerView.Adapter<*>? {
  return this.view.contentLayout.recyclerView?.adapter
}

@RestrictTo(LIBRARY_GROUP)
fun MaterialDialog.getItemSelector(): Drawable? {
  val drawable = resolveDrawable(context = context, attr = R.attr.md_item_selector)
  if (SDK_INT >= LOLLIPOP && drawable is RippleDrawable) {
    resolveColor(attr = R.attr.md_ripple_color) {
      resolveColor(attr = R.attr.colorPrimary)
          .adjustAlpha(.12f)
    }.ifNotZero {
      drawable.setColor(valueOf(it))
    }
  }
  return drawable
}

@RestrictTo(LIBRARY_GROUP)
@JvmOverloads
fun MaterialDialog.listItems(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  waitForPositiveButton: Boolean = true,
  selection: ItemListener = null
): MaterialDialog {
  assertOneSet("listItems", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()

  if (getListAdapter() != null) {
    return updateListItems(
        res = res,
        items = items,
        disabledIndices = disabledIndices,
        selection = selection
    )
  }

  return customListAdapter(
      PlainListDialogAdapter(
          dialog = this,
          items = array,
          disabledItems = disabledIndices,
          waitForPositiveButton = waitForPositiveButton,
          selection = selection
      )
  )
}

fun MaterialDialog.updateListItems(
  @ArrayRes res: Int? = null,
  items: List<CharSequence>? = null,
  disabledIndices: IntArray? = null,
  selection: ItemListener = null
): MaterialDialog {
  assertOneSet("updateListItems", items, res)
  val array = items ?: windowContext.getStringArray(res).toList()
  val adapter = getListAdapter()
  check(adapter is PlainListDialogAdapter) {
    "updateListItems(...) can't be used before you've created a plain list dialog."
  }
  adapter.replaceItems(array, selection)
  disabledIndices?.let(adapter::disableItems)
  return this
}