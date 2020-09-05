package com.app.base.utils.verify

import android.text.TextUtils
import android.view.View
import com.android.base.utils.common.StringChecker
import com.app.base.R

class ConfirmPasswordValidator private constructor(view: View, private val mNewPassword: String, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.confirm_password_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.confirm_password_error_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.password_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return !TextUtils.isEmpty(mNewPassword) && mNewPassword == content
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 8, 16)
    }

    companion object {

        fun validate(newPassword: String, view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return ConfirmPasswordValidator(view, newPassword, doOnAfterTextChanged).validate()
        }
    }

}
