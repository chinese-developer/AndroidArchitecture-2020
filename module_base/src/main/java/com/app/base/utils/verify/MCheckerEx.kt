@file:Suppress("RegExpUnexpectedAnchor")

package com.app.base.utils.verify

import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.EditText
import com.android.base.utils.StringChecker

/**是否符合手机号的长度*/
fun CharSequence?.matchCellphone(): Boolean {
    return if (this == null) {
        false
    } else {
        return StringChecker.isChinaPhoneNumber(this.toString())
    }
}

/**是否符合的密码规范(必须包含数字、字母，不允许特殊字符)*/
fun CharSequence?.matchPassword(): Boolean {
    return if (this == null) {
        false
    } else {
        eqPassword(this.toString())
    }
}

/**是否符合的密码规范(必须包含数字、字母，允许特殊字符)*/
fun CharSequence?.matchPasswordWithSpecialChar(): Boolean {
    return if (this == null) {
        false
    } else {
        eqPasswordLimited(this.toString())
    }
}

/**是否符合手机号的长度*/
fun CharSequence?.matchCellphoneLength(): Boolean {
    return if (this == null) {
        false
    } else {
        return length == 11
    }
}


/**是否符合密码长度规范*/
fun CharSequence?.matchPasswordLength(): Boolean {
    return if (this == null) {
        false
    } else {
        eqPasswordLength(this.toString())
    }
}

/**是否符合用户名长度规范*/
fun CharSequence?.matchUsernameLength(): Boolean {
    return if (this == null) {
        false
    } else {
        eqUserNameLength(this.toString())
    }
}

/**是否符合真实姓名长度规范*/
fun CharSequence?.matchRealNameLength(): Boolean {
    return if (this == null) {
        false
    } else {
        eqRealNameLength(this.toString())
    }
}

/**是否符合用户名规范*/
fun CharSequence?.matchUserName(): Boolean {
    return this?.isLegalUsername() ?: false
}

/**是否符合用户名规范（必须包含数字和字母）*/
fun CharSequence?.matchUserNameWithNumber(): Boolean {
    return this?.isLegalUsernameWithNumber() ?: false
}


/**
 * 8-16位字母、数字、限定的8种特殊字符，至少包含下面组合中一种 字母+数字，字母+特殊字符
 */
private fun eqPasswordLimited(string: String): Boolean {
    return StringChecker.containsLetterWithSpecialChar(string)
}

/**
 * 8-16位英文数字组合
 */
private fun eqPassword(string: String): Boolean {
    return StringChecker.isLengthIn(string, 8, 16)
            && StringChecker.containsDigtalLetterOnly(string)
            && StringChecker.containsDigital(string)
            && StringChecker.containsLetter(string)
}

/**8-16位*/
private fun eqPasswordLength(string: String): Boolean {
    return StringChecker.isLengthIn(string, 8, 16)
}

/**8-16位*/
private fun eqUserNameLength(string: String): Boolean {
    return StringChecker.isLengthIn(string, 6, 16)
}
/**2-15位*/
private fun eqRealNameLength(string: String): Boolean {
    return StringChecker.isLengthIn(string, 2, 15)
}

/**获取到焦点后就清空错误*/
fun EditText.clearErrorWhenHasFocus() {
    this.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
        if (hasFocus) {
            this.error = null
        }
    }
}

/**获取到焦点后就清空错误*/
fun TextInputLayout.clearErrorWhenHasFocus() {
    this.editText?.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
        if (hasFocus) {
            this.error = null
        }
    }
}

/**是否是合法的呢称（1-8字，不可以包含特殊字符）*/
fun CharSequence?.isLegalChildNickname(): Boolean {
    return this != null && !this.matches(Regex("/^((?![\\\\/:*?<>|'%]).){1,8}\$/"))
}

/**是否是合法的用户名（8-16字，不可以包含特殊字符）*/
fun CharSequence?.isLegalUsername(): Boolean {
    return this != null && !this.matches(Regex("/^[a-zA-Z0-9]{8,16}\$/;"))
}

/**是否是合法的用户名（8-16字，不可以包含特殊字符，必须包含数字和字母）*/
fun CharSequence?.isLegalUsernameWithNumber(): Boolean {
    return this != null && this.matches(Regex("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$"))
}