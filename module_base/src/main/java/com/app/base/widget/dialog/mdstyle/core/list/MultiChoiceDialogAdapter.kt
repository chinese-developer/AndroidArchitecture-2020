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
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.hasActionButtons
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColors
import com.app.base.widget.dialog.mdstyle.util.appendAll
import com.app.base.widget.dialog.mdstyle.util.createColorSelector
import com.app.base.widget.dialog.mdstyle.util.pullIndices
import com.app.base.widget.dialog.mdstyle.util.removeAll

/** 默认提供的多选适配器 */
internal class MultiChoiceDialogAdapter(
    private var dialog: MaterialDialog,
    internal var items: List<CharSequence>,
    disabledItems: IntArray?,
    initialSelection: IntArray,
    private val waitForActionButton: Boolean,
    /** 当没有任何 item 被选中时，是否允许 action buttons 可点击 */
  private val allowEmptySelection: Boolean,
    internal var selection: MultiChoiceListener
) : RecyclerView.Adapter<MultiChoiceViewHolder>(),
    DialogAdapter<CharSequence, MultiChoiceListener> {

    private var disabledIndices: IntArray = disabledItems ?: IntArray(0)
    private var currentSelection: IntArray = initialSelection
        set(value) {
            val previousSelection = field
            field = value
            for (previous in previousSelection) {
                if (!value.contains(previous)) {
                    // This value was unselected
                    notifyItemChanged(previous, UncheckPayload)
                }
            }
            for (current in value) {
                if (!previousSelection.contains(current)) {
                    // This value was selected
                    notifyItemChanged(current, CheckPayload)
                }
            }
        }

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): MultiChoiceViewHolder = MultiChoiceViewHolder.from(parent, adapter = this)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(
      holder: MultiChoiceViewHolder,
      position: Int
    ) {
        holder.isEnable = !disabledIndices.contains(position)
        holder.controlView.isChecked = currentSelection.contains(position)
        holder.titleView.text = items[position]
        holder.itemView.background = dialog.getItemSelector()

        if (dialog.bodyFont != null) {
            holder.titleView.typeface = dialog.bodyFont
        }
    }

    override fun replaceItems(
      items: List<CharSequence>,
      listener: MultiChoiceListener
    ) {
        this.items = items
        if (listener != null) {
            this.selection = listener
        }
        notifyDataSetChanged()
    }

    override fun disableItems(indices: IntArray) {
        this.disabledIndices = indices
        notifyDataSetChanged()
    }

    override fun checkItems(indices: IntArray) {
        val existingSelection = this.currentSelection
        val indicesToAdd = indices.filter { !existingSelection.contains(it) }
        this.currentSelection.appendAll(indicesToAdd)
        if (existingSelection.isEmpty()) {
            dialog.setActionButtonEnabled(POSITIVE, true)
        }
    }

    override fun uncheckItems(indices: IntArray) {
        val existingSelection = this.currentSelection
        val indicesToAdd = indices.filter { existingSelection.contains(it) }
        this.currentSelection = this.currentSelection.removeAll(indicesToAdd)
            .also {
                if (it.isEmpty()) {
                    dialog.setActionButtonEnabled(POSITIVE, allowEmptySelection)
                }
            }
    }

    override fun toggleItems(indices: IntArray) {
        val newSelection = this.currentSelection.toMutableList()
        for (target in indices) {
            if (this.disabledIndices.contains(target)) continue
            if (newSelection.contains(target)) {
                newSelection.remove(target)
            } else {
                newSelection.add(target)
            }
        }
        this.currentSelection = newSelection.toIntArray()
            .also {
                dialog.setActionButtonEnabled(
                  POSITIVE,
                  if (it.isEmpty()) allowEmptySelection else true
                )
            }
    }

    override fun checkAllItems() {
        val existingSelection = this.currentSelection
        val wholeRange = IntArray(itemCount) { it }
        val indicesToAdd = wholeRange.filter { !existingSelection.contains(it) }
        this.currentSelection = this.currentSelection.appendAll(indicesToAdd)
        if (existingSelection.isEmpty()) {
            dialog.setActionButtonEnabled(POSITIVE, true)
        }
    }

    override fun uncheckAllItems() {
        this.currentSelection = intArrayOf()
        dialog.setActionButtonEnabled(POSITIVE, allowEmptySelection)
    }

    override fun toggleAllChecked() {
        if (this.currentSelection.isEmpty()) {
            checkAllItems()
        } else {
            uncheckAllItems()
        }
    }

    override fun isItemChecked(index: Int): Boolean = currentSelection.contains(index)

    override fun positiveButtonClicked() {
        if (allowEmptySelection || currentSelection.isNotEmpty()) {
            val selectedItems = items.pullIndices(currentSelection)
            selection?.invoke(dialog, currentSelection, selectedItems)
        }
    }

    internal fun itemClicked(index: Int) {
        val newSelection = this.currentSelection.toMutableList()
        if (newSelection.contains(index)) {
            newSelection.remove(index)
        } else {
            newSelection.add(index)
        }
        this.currentSelection = newSelection.toIntArray()

        if (waitForActionButton && dialog.hasActionButtons()) {
            // 如果有 action buttons(positive&negative&neutral)，那么等待 action buttons 操作，dismiss 应交由他们处理。
            dialog.setActionButtonEnabled(
              POSITIVE,
              allowEmptySelection || currentSelection.isNotEmpty()
            )
        } else {
            // 当前没有 action buttons, 当 item 被点击后，直接调用 dismiss.
            val selectedItems = this.items.pullIndices(this.currentSelection)
            this.selection?.invoke(dialog, this.currentSelection, selectedItems)
            if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
                dialog.dismiss()
            }
        }
    }
}

internal class MultiChoiceViewHolder(
  itemView: View,
  private val adapter: MultiChoiceDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {
    init {
        itemView.setOnClickListener(this)
    }

    val controlView: AppCompatCheckBox = itemView.findViewById(R.id.md_control)
    val titleView: TextView = itemView.findViewById(R.id.md_title)

    var isEnable: Boolean
        get() = itemView.isEnabled
        set(value) {
            itemView.isEnabled = value
            controlView.isEnabled = value
            titleView.isEnabled = value
        }

    override fun onClick(view: View) = adapter.itemClicked(adapterPosition)

    companion object {
        fun from(
          parent: ViewGroup,
          adapter: MultiChoiceDialogAdapter
        ): MultiChoiceViewHolder {
            val viewHolder = MultiChoiceViewHolder(
              LayoutInflater.from(parent.context)
                .inflate(R.layout.md_listitem_multichoice, parent, false), adapter
            )
//      viewHolder.titleView.maybeSetTextColor(parent.context, R.attr.md_color_content)
            val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
            resolveColors(parent.context, attrs = widgetAttrs)
                .let {
                    CompoundButtonCompat.setButtonTintList(
                      viewHolder.controlView,
                      createColorSelector(parent.context, it[0], it[1])
                    )
                }

            return viewHolder
        }
    }
}