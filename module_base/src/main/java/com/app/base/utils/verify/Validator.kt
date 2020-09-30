package com.app.base.utils.verify

import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.base.utils.android.ResourceUtils.getString
import com.app.base.toast
import com.app.base.widget.text.ValidateCodeInputLayout

/**
 * 验证手机号的输入
 */
fun validateCellphone(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return CellphoneNumberValidator.validate(view, doOnAfterTextChanged)
}

/**
 * 验证密码输入
 */
fun validatePassword(view: View, allowSpecialCharInput: Boolean = false, doOnAfterTextChanged: Boolean = false): Boolean {
    return PasswordValidator.validate(view, allowSpecialCharInput, doOnAfterTextChanged)
}

/**
 * 验证确认密码
 */
fun validateConfirmPassword(confirmPassword: String, view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return ConfirmPasswordValidator.validate(confirmPassword, view, doOnAfterTextChanged)
}

/**
 * 验证验证码
 */
fun validateSmsCode(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return CodeValidator.validate(view, doOnAfterTextChanged)
}

/**
 * 验证昵称是否合法
 */
fun validateNickname(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return NicknameValidator.validate(view, doOnAfterTextChanged)
}

/**
 * 验证用户名是否合法
 */
fun validateUsername(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return UsernameValidator.validate(view, doOnAfterTextChanged)
}

/**
 * 验证用户名是否合法
 */
fun validateEmail(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return EmailValidator.validate(view, doOnAfterTextChanged)
}

/**
 * 验证真实姓名是否合法
 */
fun validateRealName(view: View, doOnAfterTextChanged: Boolean = false): Boolean {
    return RealNameValidator.validate(view, doOnAfterTextChanged)
}

///////////////////////////////////////////////////////////////////////////
// utils
///////////////////////////////////////////////////////////////////////////
internal fun handNoMatch(view: View, strId: Int) {
    val message = getString(strId)

    when (view) {
        is EditText -> {
            view.error = message
            if (!view.hasFocus()) {
                view.requestFocus()
            }
        }

        is TextView -> {//keep it,maybe change
            //SoftKeyboardUtils.showErrorImmediately(message, view)
            view.error = message
            if (!view.hasFocus()) {
                view.requestFocus()
            }
        }

        is TextInputLayout -> {
            if (!view.isErrorEnabled) {
                view.isErrorEnabled = true
            }
            view.error = message
        }

        is ValidateCodeInputLayout -> {
            if (!view.textInputLayout.isErrorEnabled) {
                view.textInputLayout.isErrorEnabled = true
            }
            view.textInputLayout.error = message
        }
        else -> toast(message)
    }
}

internal fun handMatch(view: View) {
    when (view) {
        is TextView -> {
            view.error = null
        }

        is TextInputLayout -> {
            if (view.isErrorEnabled) {
                view.isErrorEnabled = false
            }
            view.error = null
        }

        is ValidateCodeInputLayout -> {
            if (view.textInputLayout.isErrorEnabled) {
                view.textInputLayout.isErrorEnabled = false
            }
            view.textInputLayout.error = null
        }
        else -> {}
    }
}
