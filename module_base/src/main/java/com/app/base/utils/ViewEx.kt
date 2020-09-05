package com.app.base.utils

import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import android.text.method.LinkMovementMethod
import android.widget.EditText
import android.widget.TextView

fun TextInputLayout.textStr(): String {
    val editText = this.editText
    return editText?.text?.toString() ?: ""
}

fun EditText.textStr(): String {
    return this.text?.toString() ?: ""
}

fun TextView.enableSpanClickable() {
    // 响应点击事件的话必须设置以下属性
    movementMethod = LinkMovementMethod.getInstance()
    // 去掉点击事件后的高亮
    highlightColor = ContextCompat.getColor(context, android.R.color.transparent)
}



