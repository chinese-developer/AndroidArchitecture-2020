package com.app.base.utils.verify

import android.view.View
import com.android.base.utils.StringChecker
import com.app.base.R

class NicknameValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.msg_nick_name_empty_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.msg_child_nick_name_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.msg_child_nick_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return content.isLegalChildNickname()
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 1, 8)
    }

    companion object {
        fun validate(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return NicknameValidator(view, doOnAfterTextChanged).validate()
        }
    }

}