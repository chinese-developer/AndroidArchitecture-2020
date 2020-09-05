/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.hasActionButtons
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.util.createColorSelector
import com.app.base.widget.dialog.mdstyle.util.inflate
import com.app.base.widget.dialog.mdstyle.util.resolveColors

internal class SingleChoiceDialogAdapter(
  private val dialog: MaterialDialog,
  internal var items: List<CharSequence>,
  disabledItems: IntArray?,
  initialSelection: Int,
  private val waitForActionButton: Boolean,
  internal var selection: SingleChoiceListener
) : RecyclerView.Adapter<SingleChoiceViewHolder>(),
    DialogAdapter<CharSequence, SingleChoiceListener> {

    private var currentSelection: Int = initialSelection
        set(value) {
            if (value == field) return
            val previousSelection = field
            field = value
            notifyItemChanged(previousSelection, UncheckPayload)
            notifyItemChanged(value, CheckPayload)
        }
    private var disabledIndices: IntArray = disabledItems ?: IntArray(0)

    internal fun itemClicked(index: Int) {
        this.currentSelection = index
        if (waitForActionButton && dialog.hasActionButtons()) {
            dialog.setActionButtonEnabled(POSITIVE, true)
        } else {
            this.selection?.invoke(dialog, index, items[index])
            if (dialog.autoDismissEnabled && !dialog.hasActionButtons()) {
                dialog.dismiss()
            }
        }
    }

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): SingleChoiceViewHolder {
        val listItemView: View =
            parent.inflate(dialog.windowContext, R.layout.md_listitem_singlechoice)
        val viewHolder = SingleChoiceViewHolder(
          itemView = listItemView,
          adapter = this
        )

//    viewHolder.titleView.maybeSetTextColor(dialog.windowContext, R.attr.md_color_content)

        val widgetAttrs = intArrayOf(R.attr.md_color_widget, R.attr.md_color_widget_unchecked)
        dialog.resolveColors(attrs = widgetAttrs)
            .let {
                CompoundButtonCompat.setButtonTintList(
                  viewHolder.controlView,
                  createColorSelector(dialog.windowContext, checked = it[0], unchecked = it[1])
                )
            }

        return viewHolder
    }

    override fun onBindViewHolder(
      holder: SingleChoiceViewHolder,
      position: Int
    ) {
        holder.isEnabled = !disabledIndices.contains(position)
        holder.controlView.isChecked = currentSelection == position

        holder.titleView.text = items[position]

        holder.itemView.background = dialog.getItemSelector()

        if (dialog.bodyFont != null) {
            holder.titleView.typeface = dialog.bodyFont
        }
    }

    override fun onBindViewHolder(
      holder: SingleChoiceViewHolder,
      position: Int,
      payloads: MutableList<Any>
    ) {
        when (payloads.firstOrNull()) {
          CheckPayload -> {
            holder.controlView.isChecked = true
            return
          }
          UncheckPayload -> {
            holder.controlView.isChecked = false
            return
          }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = items.size

    override fun replaceItems(
      items: List<CharSequence>,
      listener: SingleChoiceListener
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

    override fun checkItems(indices: IntArray) {
        val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
        if (this.disabledIndices.contains(targetIndex)) return
        this.currentSelection = targetIndex
    }

    override fun uncheckItems(indices: IntArray) {
        val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
        if (this.disabledIndices.contains(targetIndex)) return
        this.currentSelection = -1
    }

    override fun toggleItems(indices: IntArray) {
        val targetIndex = if (indices.isNotEmpty()) indices[0] else -1
        if (this.disabledIndices.contains(targetIndex)) return
        if (indices.isEmpty() || this.currentSelection == targetIndex) {
            this.currentSelection = -1
        } else {
            this.currentSelection = targetIndex
        }
    }

    override fun positiveButtonClicked() {
        if (currentSelection > -1) {
            selection?.invoke(dialog, currentSelection, items[currentSelection])
        }
    }

    override fun checkAllItems() = Unit

    override fun uncheckAllItems() = Unit

    override fun toggleAllChecked() = Unit

    override fun isItemChecked(index: Int): Boolean = this.currentSelection == index
}

internal class SingleChoiceViewHolder(
  itemView: View,
  private val adapter: SingleChoiceDialogAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    val controlView: AppCompatRadioButton = itemView.findViewById(R.id.md_control)
    val titleView: TextView = itemView.findViewById(R.id.md_title)

    var isEnabled: Boolean
        get() = itemView.isEnabled
        set(value) {
            itemView.isEnabled = value
            controlView.isEnabled = value
            titleView.isEnabled = value
        }

    override fun onClick(v: View?) = adapter.itemClicked(adapterPosition)
}