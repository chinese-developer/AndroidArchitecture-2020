package com.app.base.widget.dialog

import android.view.View
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import com.app.base.widget.dialog.BaseDialogBuilder.Companion.NO_ID
import kotlinx.android.synthetic.main.dialog_confirm_layout.*

internal class ConfirmDialog(builder: ConfirmDialogBuilder) : BaseDialog(builder.context, builder.style) {

    init {
        setContentView(R.layout.dialog_confirm_layout)
        applyBuilder(builder)
    }

    private fun applyBuilder(builder: ConfirmDialogBuilder) {
        //icon
        if (builder.iconId != NO_ID) {
            ivConfirmDialogIcon.visible()
            ivConfirmDialogIcon.setImageResource(builder.iconId)
        } else {
            ivConfirmDialogIcon.gone()
        }

        //title
        val title = builder.title
        if (title != null) {
            tvConfirmDialogTitle.visible()
            tvConfirmDialogTitle.text = title
            tvConfirmDialogTitle.textSize = builder.titleSize
            tvConfirmDialogTitle.setTextColor(builder.titleColor)
        }

        //message
        tvConfirmDialogMessage.text = builder.message
        tvConfirmDialogMessage.textSize = builder.messageSize
        tvConfirmDialogMessage.setTextColor(builder.messageColor)
        //如果没有 title，则 message 为粗体
        if (tvConfirmDialogTitle.visibility != View.VISIBLE) {
            tvConfirmDialogMessage.paint.isFakeBoldText = true
            tvConfirmDialogMessage.invalidate()
        }

        //cancel
        val negativeText = builder.negativeText
        if (negativeText != null) {
            dblDialogBottom.negativeText(negativeText)
            dblDialogBottom.onNegativeClick(View.OnClickListener {
                dismissChecked(builder)
                builder.negativeListener?.invoke(this)
            })
        } else {
            dblDialogBottom.hideNegative()
        }

        //confirm
        dblDialogBottom.positiveText(builder.positiveText)
        if (builder.positiveColor != NO_ID) {
            dblDialogBottom.positiveColor(builder.positiveColor)
        }
        dblDialogBottom.onPositiveClick(View.OnClickListener {
            dismissChecked(builder)
            builder.positiveListener?.invoke(this)
        })

    }

    private fun dismissChecked(builder: ConfirmDialogBuilder) {
        if (builder.autoDismiss) {
            dismiss()
        }
    }

}
