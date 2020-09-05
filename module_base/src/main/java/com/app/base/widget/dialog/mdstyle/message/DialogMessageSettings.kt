/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.message

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.StringRes
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.message.LinkTransformationMethod
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveString

class DialogMessageSettings internal constructor(
  private val dialog: MaterialDialog,
  @Suppress("MemberVisibilityCanBePrivate")
  val messageTextView: TextView
) {
    private var isHtml: Boolean = false

    fun lineSpacing(multiplier: Float): DialogMessageSettings {
        messageTextView.setLineSpacing(0f, multiplier)
        return this
    }

    fun html(onLinkClick: ((link: String) -> Unit)? = null): DialogMessageSettings {
        isHtml = true
        if (onLinkClick != null) {
            messageTextView.transformationMethod = LinkTransformationMethod(onLinkClick)
        }
        messageTextView.movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    internal fun setText(
      @StringRes res: Int?,
      text: CharSequence?
    ) {
        messageTextView.text = text.maybeWrapHtml(isHtml)
            ?: resolveString(dialog, res, html = isHtml)
    }

    private fun CharSequence?.maybeWrapHtml(isHtml: Boolean): CharSequence? {
        if (this == null) return null
        @Suppress("DEPRECATION")
        return if (isHtml) Html.fromHtml(this.toString()) else this
    }
}