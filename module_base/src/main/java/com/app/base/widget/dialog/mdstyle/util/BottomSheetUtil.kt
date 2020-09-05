/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import java.lang.Float.isNaN
import kotlin.math.abs

@Suppress("DEPRECATION")
internal fun BottomSheetBehavior<*>.setCallbacks(
    onSlide: (currentHeight: Int) -> Unit,
    onHide: () -> Unit
) {
    setBottomSheetCallback(object : BottomSheetCallback() {
        private var currentState: Int = STATE_COLLAPSED

        override fun onSlide(
            bottomSheet: View,
            dY: Float
        ) {
            if (state == STATE_HIDDEN) return
            // 如果指定的值是非数字的值 返回true, 否则返回 false.
            val percentage = if (isNaN(dY)) 0f else dY
            if (percentage > 0f) {
                val diff = peekHeight * abs(percentage)
                onSlide((peekHeight + diff).toInt())
            } else {
                val diff = peekHeight * abs(percentage)
                onSlide((peekHeight - diff).toInt())
            }
        }

        override fun onStateChanged(
            bottomSheet: View,
            newState: Int
        ) {
            currentState = state
            if (state == STATE_HIDDEN) onHide()
        }
    })
}

internal fun BottomSheetBehavior<*>.animatePeekHeight(
    view: View,
    start: Int = peekHeight,
    dest: Int,
    duration: Long,
    onEnd: () -> Unit = {}
) {
    if (dest == start) {
        return
    } else if (duration <= 0) {
        peekHeight = dest
        return
    }
    val animator = animateValues(
        from = start,
        to = dest,
        duration = duration,
        onUpdate = this::setPeekHeight,
        onEnd = onEnd
    )
    view.onDetach { animator.cancel() }
    animator.start()
}
