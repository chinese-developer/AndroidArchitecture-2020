package com.app.base.utils.verify

import android.view.View

import com.android.base.utils.StringChecker
import com.app.base.R

class RealNameValidator private constructor(view: View, doOnAfterTextChanged: Boolean) : TextValidator(view, doOnAfterTextChanged) {

    public override fun emptyTips(): Int {
        return R.string.real_name_empty_tips
    }

    public override fun noMatchTips(): Int {
        return R.string.real_name_error_tips
    }

    override fun lengthOverLimitTips(): Int {
        return R.string.real_name_length_ove_limit_tips
    }

    override fun validateTypeText(content: String): Boolean {
        return StringChecker.isChinese(content)
    }

    override fun validateTextLength(content: String): Boolean {
        return content.matchRealNameLength()
    }

    companion object {

        fun validate(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
            return RealNameValidator(view, doOnAfterTextChanged).validate()
        }
    }

}
