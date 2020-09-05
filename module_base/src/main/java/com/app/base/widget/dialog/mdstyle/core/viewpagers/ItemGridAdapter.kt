/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.viewpagers

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.widget.dialog.mdstyle.core.widget.ColorRectangleView
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.hasActionButtons
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.core.list.getItemSelector
import com.app.base.widget.dialog.mdstyle.util.MDUtil.isColorDark
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColor
import com.app.base.widget.dialog.mdstyle.util.setVisibleOrGone

internal class ItemGridAdapter(
  private val dialog: MaterialDialog,
  private val items: IntArray,
  private val subItems: Array<IntArray>?,
  private val initialSelection: Int?,
  private val waitForPositiveButton: Boolean,
  private val callback: MaterialDialogItemCallback,
  private val enableSliding: Boolean
) : RecyclerView.Adapter<ItemGridHolder>() {

    private val upIcon =
        if (resolveColor(
            dialog.windowContext,
            attr = android.R.attr.textColorPrimary
          ).isColorDark()
        )
            R.drawable.md_icon_back_black
        else R.drawable.md_icon_back_white

    private val customIcon =
        if (resolveColor(
            dialog.windowContext,
            attr = android.R.attr.textColorPrimary
          ).isColorDark()
        )
            R.drawable.md_icon_custom_black
        else R.drawable.md_icon_custom_white

    private var selectedTopIndex: Int = -1
    private var selectedSubIndex: Int = -1
    private var inSub: Boolean = false

    init {
        initialSelection?.let(this::updateSelection)
    }

    override fun onCreateViewHolder(
      parent: ViewGroup,
      viewType: Int
    ): ItemGridHolder {
        val layoutRes =
            if (viewType == 1) R.layout.md_grid_item_go_up
            else R.layout.md_grid_item
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)
        view.background = dialog.getItemSelector()
        return ItemGridHolder(view, this)
    }

    override fun onBindViewHolder(
      holder: ItemGridHolder,
      position: Int
    ) {
        if (inSub && position == 0) {
            holder.iconView.setImageResource(upIcon)
            return
        }
        if (enableSliding && !inSub && position == itemCount - 1) {
            holder.iconView.setImageResource(customIcon)
            return
        }

        val color =
            if (inSub) subItems!![selectedTopIndex][position - 1]
            else items[position]

        holder.colorRectangle?.color = color
        holder.colorRectangle?.border =
            resolveColor(holder.itemView.context, attr = android.R.attr.textColorPrimary)

        holder.iconView.setImageResource(
          if (color.isColorDark()) R.drawable.md_icon_checkmark_white
          else R.drawable.md_icon_checkmark_black
        )
        holder.iconView.setVisibleOrGone(
          if (inSub) position == selectedSubIndex
          else position == selectedTopIndex
        )
    }

    override fun getItemCount(): Int =
        if (inSub) subItems!![selectedTopIndex].size + 1
        else items.size + (if (enableSliding) 1 else 0)

    override fun getItemViewType(position: Int): Int {
        if (inSub && position == 0) { // upIcon
            return 1
        }
        if (enableSliding && !inSub && position == itemCount - 1) {
            return 1
        }
        return 0
    }

    fun itemSelected(index: Int) {
        if (inSub && index == 0) {
            inSub = false
            notifyDataSetChanged()
            return
        }
        if (enableSliding && !inSub && index == itemCount - 1) {
            dialog.setPage(1)
            return
        }

        dialog.setActionButtonEnabled(POSITIVE, true)

        if (inSub) {
            val previousSelection = selectedSubIndex
            selectedSubIndex = index
            notifyItemChanged(previousSelection)
            notifyItemChanged(selectedSubIndex)
            onColorSelected()
            return
        }

        if (index != selectedTopIndex) {
            // 与之前选择的上层不同，说明当前处于上层，此时需要重置子元素。
            selectedSubIndex = -1
        }

        selectedTopIndex = index
        if (subItems != null) {
            inSub = true
            // 当还没有选择 sub 层颜色时，需要将上层颜色作为当前选中值
            selectedSubIndex =
                subItems[selectedTopIndex].indexOfFirst { it == items[selectedTopIndex] }
            if (selectedSubIndex > -1) {
                // 因为存在 go-up button，index 需递增 1
                selectedSubIndex++
            }
        }

        onColorSelected()
        notifyDataSetChanged()
    }

    fun selectedColor(): Int? {
        if (selectedTopIndex > -1) {
            if (selectedSubIndex > -1 && subItems != null) {
                return subItems[selectedTopIndex][selectedSubIndex - 1]
            }
            return items[selectedTopIndex]
        }
        return null
    }

    private fun onColorSelected() {
        val selectedColor = selectedColor() ?: 0
        val actualWaitForPositive = waitForPositiveButton && dialog.hasActionButtons()
        if (!actualWaitForPositive) {
            callback?.invoke(dialog, selectedColor)
        }
        dialog.updateActionButtonsColor(selectedColor)
    }

    internal fun updateSelection(@ColorInt color: Int) {
        selectedTopIndex = items.indexOfFirst { it == color }
        if (subItems != null) {
            for (section in subItems.indices) {
                selectedSubIndex = subItems[section].indexOfFirst { it == color }
                inSub = selectedSubIndex != -1
                if (inSub) {
                    selectedSubIndex++ // go-up arrow
                    selectedTopIndex = section
                    break
                }
            }
        }
        notifyDataSetChanged()
    }
}

internal class ItemGridHolder(
  itemView: View,
  private val adapter: ItemGridAdapter
) : RecyclerView.ViewHolder(itemView), OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    val iconView: ImageView = itemView.findViewById(R.id.icon)
    val colorRectangle: ColorRectangleView? = itemView.findViewById(R.id.color_view)

    override fun onClick(v: View?) = adapter.itemSelected(adapterPosition)
}