package com.app.base.widget.dialog

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.android.UnitConverter
import com.app.base.R
import kotlinx.android.synthetic.main.dialog_list_layout.*

/**
 * 列表对话框
 *
 */
internal class ListDialog(listDialogBuilder: ListDialogBuilder) :
    BaseDialog(listDialogBuilder.context, listDialogBuilder.style) {

    private var mSelectedItemIndex: Int = 0
    private var mSelectableBgId = 0

    init {
        setContentView(R.layout.dialog_list_layout)
        getSelectedBg()
        applyListDialogBuilder(listDialogBuilder)
    }

    private fun applyListDialogBuilder(listDialogBuilder: ListDialogBuilder) {
        //cancel
        dblListDialogBottom.negativeText(listDialogBuilder.negativeText)
        dblListDialogBottom.onNegativeClick(View.OnClickListener {
            checkDismiss(listDialogBuilder)
            listDialogBuilder.negativeListener?.invoke()
        })

        //confirm
        dblListDialogBottom.positiveText(listDialogBuilder.positiveText)
        if (listDialogBuilder.positiveColor != BaseDialogBuilder.NO_ID) {
            dblListDialogBottom.positiveColor(listDialogBuilder.positiveColor)
        }

        //list
        rvDialogListContent.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        val items = listDialogBuilder.items
        val adapter = listDialogBuilder.adapter

        if (items != null) {

            val size = items.size
            mSelectedItemIndex = listDialogBuilder.selectedPosition
            if (mSelectedItemIndex < 0 || mSelectedItemIndex >= size) {
                mSelectedItemIndex = size - 1
            }
            rvDialogListContent.adapter = Adapter(context, items.toList())
            dblListDialogBottom.onPositiveClick(View.OnClickListener {
                checkDismiss(listDialogBuilder)
                listDialogBuilder.positiveListener?.invoke(
                    mSelectedItemIndex,
                    items[mSelectedItemIndex]
                )
            })

        } else if (adapter != null) {
            rvDialogListContent.adapter = adapter
            dblListDialogBottom.onPositiveClick(View.OnClickListener {
                checkDismiss(listDialogBuilder)
                listDialogBuilder.positiveListener?.invoke(-1, "")
            })
        }
    }

    private fun checkDismiss(listDialogBuilder: ListDialogBuilder) {
        if (listDialogBuilder.autoDismiss) {
            dismiss()
        }
    }

    private fun getSelectedBg() {
        try {
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            mSelectableBgId = outValue.resourceId
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class Adapter internal constructor(
        val context: Context,
        val data: List<CharSequence>?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val mOnClickListener = View.OnClickListener { view ->
            val childAdapterPosition = rvDialogListContent!!.getChildAdapterPosition(view)
            val oldSelectedItemIndex = mSelectedItemIndex
            mSelectedItemIndex = childAdapterPosition
            notifyItemChanged(oldSelectedItemIndex)
            notifyItemChanged(mSelectedItemIndex)
        }

        private val hPadding = UnitConverter.dpToPx(19)
        private val vPadding = UnitConverter.dpToPx(12)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val textView = AppCompatTextView(context)
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sel_choice, 0, 0, 0)
            textView.compoundDrawablePadding = UnitConverter.dpToPx(10)
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (mSelectableBgId != 0) {
                textView.setBackgroundResource(mSelectableBgId)
            }
            textView.setPadding(hPadding, vPadding, hPadding, vPadding)
            return ListViewHolder(textView)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            if (data.isNullOrEmpty()) return
            (viewHolder.itemView as TextView).text = data[position]
            viewHolder.itemView.isSelected = viewHolder.bindingAdapterPosition == mSelectedItemIndex
            if (!viewHolder.itemView.isSelected) {
                viewHolder.itemView.setOnClickListener(mOnClickListener)
            } else {
                viewHolder.itemView.setOnClickListener(null)
            }
        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }

        inner class ListViewHolder(textView: TextView) : RecyclerView.ViewHolder(textView)
    }

}
