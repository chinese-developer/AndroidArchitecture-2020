package com.app.base.utils.verify

import android.view.View
import com.android.base.utils.common.StringChecker
import com.app.base.R

class UsernameValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.username_empty_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.username_name_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.username_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        val legalUsername = content.isLegalUsernameWithNumber()
        return legalUsername
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 8, 16)
    }

    companion object {
        fun validate(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return UsernameValidator(view, doOnAfterTextChanged).validate()
        }
    }

}