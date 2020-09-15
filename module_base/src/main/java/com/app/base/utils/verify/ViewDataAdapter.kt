package com.app.base.utils.verify

import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.TextView
import com.app.base.widget.text.ValidateCodeInputLayout
import com.app.base.utils.ext.textStr

internal fun getStringData(view: View): String {
    return when (view) {
        is TextView -> view.text.toString()
        is TextInputLayout -> view.textStr()
        is ValidateCodeInputLayout -> view.textInputLayout.textStr()
        else -> {
            throw IllegalArgumentException("ViewDataAdapter unSupport ")
        }
    }
}