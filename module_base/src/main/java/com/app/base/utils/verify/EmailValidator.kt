package com.app.base.utils.verify

import android.view.View
import com.android.base.utils.StringChecker
import com.app.base.R

class EmailValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.msg_email_empty_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.msg_email_error_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.msg_email_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return StringChecker.isEmail(content)
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 4, 50)
    }

    companion object {
        fun validate(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return EmailValidator(view, doOnAfterTextChanged).validate()
        }
    }

}