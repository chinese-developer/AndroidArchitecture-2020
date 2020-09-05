package com.app.base.utils.verify

import android.view.View
import com.android.base.utils.common.StringChecker
import com.app.base.R

class CodeValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.sms_code_enter_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.sms_code_no_match_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.sms_code_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return content.isNotEmpty()
    }

    override fun validateTextLength(content: String): Boolean {
        return StringChecker.isLengthIn(content, 4, 6)
    }

    companion object {

        fun validate(codeIil: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return CodeValidator(codeIil, doOnAfterTextChanged).validate()
        }
    }

}
