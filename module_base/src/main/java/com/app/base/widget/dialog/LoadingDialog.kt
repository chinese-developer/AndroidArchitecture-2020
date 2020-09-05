package com.app.base.widget.dialog

import androidx.appcompat.app.AppCompatDialog
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.visible
import com.app.base.R
import kotlinx.android.synthetic.main.dialog_loading.*

internal class LoadingDialog constructor(builder: BaseLoadingDialog) : AppCompatDialog(builder.context, builder.style) {

    init {
        setContentView(R.layout.dialog_loading)
        applyBuilder(builder)
    }

    private fun applyBuilder(builder: BaseLoadingDialog) {
        if (builder.title.isNullOrEmpty()) {
            tvDialogLoadingTitle.gone()
        } else {
            tvDialogLoadingTitle.visible()
        tvDialogLoadingTitle.text = builder.title
        tvDialogLoadingTitle.textSize = builder.titleSize
        tvDialogLoadingTitle.setTextColor(builder.titleColor)
        }
    }
    override fun setTitle(title: CharSequence?) {
        if (title?.isEmpty() == true) {
            tvDialogLoadingTitle.gone()
        } else {
            tvDialogLoadingTitle.visible()
            tvDialogLoadingTitle.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        setTitle(context.getText(titleId))
    }
}

