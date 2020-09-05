/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.rtl

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.app.base.widget.dialog.mdstyle.util.MDUtil.setGravityStartCompat

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-26 14:31
 *
 *      在我们使用自定义布局时，设置 START/END gravity 无效，因此我们手动设置 RTL / LTR 的文本对齐方式。
 */
class RtlTextView(
  context: Context,
  attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {
    init {
        setGravityStartCompat()
    }
}