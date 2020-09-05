package com.app.base.widget.dialog

import android.content.Context
import androidx.annotation.NonNull
import com.android.base.app.ui.LoadingView
import com.app.base.R
import com.app.base.toast

class AppLoadingView constructor(@NonNull context: Context) : LoadingView {

    private val mContext = context

    private var mLoadingDialog: LoadingDialog = showLoadingDialog(context) {} as LoadingDialog

    override fun showLoadingDialog(message: CharSequence?, cancelable: Boolean) {
        if (!message.isNullOrEmpty()) {
            mLoadingDialog.setTitle(message)
        } else {
            mLoadingDialog.setTitle(R.string.dialog_loading)
        }
        mLoadingDialog.setCancelable(cancelable)
        mLoadingDialog.show()
    }

    override fun showLoadingDialog(messageId: Int, cancelable: Boolean) {
        showLoadingDialog(mContext.getText(messageId), cancelable)
    }

    override fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

    override fun showMessage(message: CharSequence) {
        toast(message)
    }

    override fun showMessage(messageId: Int) {
        toast(mContext.getText(messageId))
    }

    override fun showLoadingDialog(cancelable: Boolean) {
        showLoadingDialog(null, cancelable)
    }

    override fun showLoadingDialog() {
        showLoadingDialog(null, false)
    }

}