/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.base.utils.ktx

import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView


fun ScrollView.scrollToBottom() {
    fullScroll(View.FOCUS_DOWN)
}

fun ScrollView.scrollToTop() {
    fullScroll(View.FOCUS_UP)
}

fun NestedScrollView.scrollToBottom() {
    fullScroll(View.FOCUS_DOWN)
}

fun NestedScrollView.scrollToTop() {
    fullScroll(View.FOCUS_UP)
}
