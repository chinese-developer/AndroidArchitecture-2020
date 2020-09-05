/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.message

import android.text.style.URLSpan
import android.view.View

class CustomUrlSpan(
  url: String,
  private val onLinkClick: (String) -> Unit
) : URLSpan(url) {
    override fun onClick(widget: View) = onLinkClick(url)
}
