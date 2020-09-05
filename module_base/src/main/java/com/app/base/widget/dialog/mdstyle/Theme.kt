/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle

import android.content.Context
import androidx.annotation.CheckResult
import androidx.annotation.StyleRes
import com.app.base.widget.dialog.mdstyle.core.DialogBehavior
import com.app.base.widget.dialog.mdstyle.util.MDUtil.isColorDark
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColor

@StyleRes @CheckResult
internal fun inferTheme(
    context: Context,
    dialogBehavior: DialogBehavior
): Int {
    val isThemeDark = !inferThemeIsLight(context)
    return dialogBehavior.getThemeRes(isThemeDark)
}

@CheckResult internal fun inferThemeIsLight(context: Context): Boolean =
    resolveColor(
        context = context,
        attr = android.R.attr.textColorPrimary
    ).isColorDark()