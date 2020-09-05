package com.app.base.utils.verify

import android.view.View
import com.android.base.utils.common.StringChecker
import com.app.base.R

class PasswordValidator private constructor(view: View, private val allowSpecialCharInput: Boolean, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.password_enter_tips
    }

    public override fun noMatchTips(): Int {
        return if (allowSpecialCharInput) {
            R.string.password_no_match_special_char_tips
        } else {
            R.string.password_no_match_tips
        }
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.password_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return if (allowSpecialCharInput) {
            content.matchPasswordWithSpecialChar()
        } else {
            content.matchPassword()
        }
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 8, 16)
    }

    companion object {
        fun validate(view: View, allowSpecialCharInput: Boolean = false, doOnAfterTextChanged: Boolean = false): Boolean {
            return PasswordValidator(view, allowSpecialCharInput, doOnAfterTextChanged).validate()
        }
    }

}
