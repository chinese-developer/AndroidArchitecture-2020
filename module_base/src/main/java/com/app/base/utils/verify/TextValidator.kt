package com.app.base.utils.verify

import android.text.TextUtils
import android.view.View

abstract class TextValidator(view: View, private val doOnAfterTextChanged: Boolean) : AbsValidator(view) {

    override val isMatch: Boolean
        get() {
            val validateData = validateData
            return if (doOnAfterTextChanged) {
                matchAfterTextChanged(validateData)
            } else {
                matchWhenButtonClicked(validateData)
            }
        }

    override val validateData: String
        get() = getStringData(validateView)

    protected abstract fun validateTypeText(content: String): Boolean

    protected abstract fun validateTextLength(content: String): Boolean

    internal abstract fun emptyTips(): Int

    internal abstract fun noMatchTips(): Int

    internal abstract fun lengthOverLimitTips(): Int

    private fun matchAfterTextChanged(validateData: String): Boolean {
        if (!validateTextLength(validateData)) {
            handNoMatch(validateView, lengthOverLimitTips())
        } else if (!validateTypeText(validateData)) {
            handNoMatch(validateView, noMatchTips())
        } else {
            handMatch(validateView)
            return true
        }
        return false
    }

    private fun matchWhenButtonClicked(validateData: String): Boolean {
        if (TextUtils.isEmpty(validateData)) {
            handNoMatch(validateView, emptyTips())
        } else if (!validateTypeText(validateData)) {
            handNoMatch(validateView, noMatchTips())
        } else {
            handMatch(validateView)
            return true
        }
        return false
    }

}
