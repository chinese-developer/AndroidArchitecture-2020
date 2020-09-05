/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.list

import com.app.base.widget.dialog.mdstyle.MaterialDialog

typealias ItemListener =
        ((dialog: MaterialDialog, indices: Int, text: CharSequence) -> Unit)?

typealias MultiChoiceListener =
        ((dialog: MaterialDialog, indices: IntArray, items: List<CharSequence>) -> Unit)?

typealias SingleChoiceListener =
        ((dialog: MaterialDialog, index: Int, text: CharSequence) -> Unit)?