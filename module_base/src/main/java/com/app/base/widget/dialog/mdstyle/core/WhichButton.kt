/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core

import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButtonLayout.Companion.INDEX_NEGATIVE
import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButtonLayout.Companion.INDEX_NEUTRAL
import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButtonLayout.Companion.INDEX_POSITIVE

enum class WhichButton(val index: Int) {
    POSITIVE(INDEX_POSITIVE),
    NEGATIVE(INDEX_NEGATIVE),
    NEUTRAL(INDEX_NEUTRAL);

    companion object {
        fun fromIndex(index: Int): WhichButton = when (index) {
          INDEX_POSITIVE -> POSITIVE
          INDEX_NEGATIVE -> NEGATIVE
          INDEX_NEUTRAL -> NEUTRAL
            else -> throw IndexOutOfBoundsException("$index is not an action button index.")
        }
    }
}