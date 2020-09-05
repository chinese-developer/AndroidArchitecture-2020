/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.hasActionButtons
import com.app.base.widget.dialog.mdstyle.util.MDUtil.maybeSetTextColor

private const val KEY_ACTIVATED_INDEX = "activated_index"

internal class PlainListDialogAdapter(
  private var dialog: MaterialDialog,
  internal var items: List<CharSequence>,
  disabledItems: IntArray?,
  private var waitForPositiveButton: Boolean,
  internal var selection: ItemListener
) : RecyclerView.Adapter<PlainListViewHolder>(), DialogAdapter<CharSequence, ItemListener> {

  private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PlainListViewHolder = PlainListViewHolder.from(parent, adapter = this)

  override fun onBindViewHolder(
    holder: PlainListViewHolder,
    position: Int
  ) {
    holder.itemView.isEnabled = !disabledIndices.contains(position)

    val titleValue = items[position]
    holder.titleView.text = titleValue
    holder.itemView.background = dialog.getItemSelector()

    val activatedIndex = dialog.config[KEY_ACTIVATED_INDEX] as? Int
    holder.itemView.isActivated = activatedIndex != null && activatedIndex == position

    if (dialog.bodyFont != null) {
      holder.titleView.typeface = dialog.bodyFont
    }
  }

  override fun replaceItems(
    items: List<CharSequence>,
    listener: ItemListener
  ) {
    this.items = items
    if (listener != null) {
      this.selection = listener
    }
    this.notifyDataSetChanged()
  }

  override fun disableItems(indices: IntArray) {
    this.disabledIndices = indices
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = items.size

  override fun positiveButtonClicked() {
    val activatedIndex = dialog.config[KEY_ACTIVATED_INDEX] as? Int
    if (activatedIndex != null) {
      selection?.invoke(dialog, activatedIndex, items[activatedIndex])
      dialog.config.remove(KEY_ACTIVATED_INDEX)
    }
  }

  fun itemClicked(index: Int) {
    if (waitForPositiveButton && dialog.hasActionButtons(POSITIVE)) {
      // 如果有 action buttons(positive&negative&neutral)，那么等待 action buttons 操作，dismiss 应交由他们处理。
      val lastActivated = dialog.config[KEY_ACTIVATED_INDEX] as? Int
      dialog.config[KEY_ACTIVATED_INDEX] = index
      if (lastActivated != null) {
        notifyItemChanged(lastActivated)
      }
      notifyItemChanged(index)
    } else {
      this.selection?.invoke(dialog, index, this.items[index])
      if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
        dialog.dismiss()
      }
    }
  }

  override fun checkItems(indices: IntArray) = Unit

  override fun uncheckItems(indices: IntArray) = Unit

  override fun toggleItems(indices: IntArray) = Unit

  override fun checkAllItems() = Unit

  override fun uncheckAllItems() = Unit

  override fun toggleAllChecked() = Unit

  override fun isItemChecked(index: Int): Boolean = false
}

internal class PlainListViewHolder(
  itemView: View,
  private val adapter: PlainListDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {
  init {
    itemView.setOnClickListener(this)
  }

  companion object {
    fun from(
      parent: ViewGroup,
      adapter: PlainListDialogAdapter
    ): PlainListViewHolder {
      val viewHolder = PlainListViewHolder(
          LayoutInflater.from(parent.context).inflate(R.layout.md_listitem, parent, false),
          adapter = adapter
      )
      viewHolder.titleView.maybeSetTextColor(parent.context, R.attr.md_color_content)
      return viewHolder
    }
  }

  val titleView = (itemView as ViewGroup).getChildAt(0) as TextView

  override fun onClick(v: View?) = adapter.itemClicked(adapterPosition)

}