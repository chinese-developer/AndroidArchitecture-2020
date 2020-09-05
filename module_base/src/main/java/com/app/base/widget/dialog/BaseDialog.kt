package com.app.base.widget.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.app.base.R
import com.blankj.utilcode.util.ScreenUtils

open class BaseDialog(
        context: Context,
        style: Int = R.style.Theme_Dialog_Common_Transparent_Floating
) : AppCompatDialog(context, style) {

    companion object {
        const val DEFAULT_SIZE_PERCENT = 0.75F
    }

    init {
        setCommonWindowAttributes()
    }

    private fun setCommonWindowAttributes() {
        window?.run {
            attributes.windowAnimations = R.style.Style_Anim_Dialog_Fade_In
        }
    }

    protected open var maxDialogWidthPercent = DEFAULT_SIZE_PERCENT
    protected open var maxDialogHeightPercent = DEFAULT_SIZE_PERCENT

    override fun show() {

        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        val realScreenWidth = if (screenWidth < screenHeight) screenWidth else screenHeight
        val realScreenHeight = if (screenWidth < screenHeight) screenHeight else screenWidth

        window?.run {
            attributes = attributes.also {
                it.width = (realScreenWidth * maxDialogWidthPercent).toInt()
            }
        }

        super.show()

        window?.decorView?.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            val maxHeight = (maxDialogHeightPercent * realScreenHeight).toInt()
            if (v.height > maxHeight) {
                window?.attributes = window?.attributes?.also {
                    it.height = maxHeight
                }
            }
        }
    }

}