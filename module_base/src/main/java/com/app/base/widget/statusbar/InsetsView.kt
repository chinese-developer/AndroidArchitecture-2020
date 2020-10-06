package com.app.base.widget.statusbar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.android.base.utils.android.compat.AndroidVersion
import com.app.base.R
import com.blankj.utilcode.util.BarUtils.getStatusBarHeight

/**
 * StatusBar
 */
class InsetsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        if (background == null) {
            background = ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (AndroidVersion.atLeast(19)) {
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(getStatusBarHeight(), MeasureSpec.EXACTLY)
            )
        } else {
            super.onMeasure(widthMeasureSpec, 0)
        }
    }

}