package com.app.base.utils.verify

import android.view.View

import com.android.base.utils.StringChecker
import com.app.base.R

class CellphoneNumberValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.msg_cellphone_enter_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.msg_cellphone_no_match_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.msg_cellphone_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return StringChecker.isChinaPhoneNumber(content)
    }

    override fun validateTextLength(content: String): Boolean {
        return content.matchCellphoneLength()
    }

    companion object {

        fun validate(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return CellphoneNumberValidator(view, doOnAfterTextChanged).validate()
        }
    }

}
