/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.android.base.utils.ktx.dpToPx
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.dimenPx

internal class ColorRectangleView(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val rectangleDefault = 56
        private const val boundRadiusX = 20f
        private const val boundRadiusY = 15f
    }

    private val strokePaint = Paint()
    private val fillPaint = Paint()
    private val rectF: RectF =
        RectF(0f, 0f, dpToPx(rectangleDefault.toFloat()), dpToPx(rectangleDefault.toFloat()))

    private val borderWidth = dimenPx(
      R.dimen.md_view_border
    )

    private var transparentGrid: Drawable? = null

    init {
        // 去掉 WILL_NOT_DRAW FLAG, 为了重写 onDraw.
        setWillNotDraw(false)
        // 开启抗齿距, 图片变得平滑, 但会降低一点点图片的清晰度.
        strokePaint.isAntiAlias = true
        // 是否开启图像抖动. 当图片的像素配置和手机屏幕的像素配置不一致时,设置为true, 高质量的图片在低像素的手机屏幕上也能比较好的显示.
        strokePaint.isDither = true
        strokePaint.style = STROKE
        strokePaint.color = Color.BLACK
        strokePaint.strokeWidth = borderWidth.toFloat()
        fillPaint.style = FILL
        fillPaint.isAntiAlias = true
        fillPaint.isDither = true
        fillPaint.color = Color.DKGRAY
    }

    @ColorInt var color: Int = Color.BLACK
        set(value) {
            field = value
            fillPaint.color = value
            invalidate()
        }

    @ColorInt var border: Int = Color.DKGRAY
        set(value) {
            field = value
            strokePaint.color = value
            invalidate()
        }

    override fun onMeasure(
      widthMeasureSpec: Int,
      heightMeasureSpec: Int
    ) = super.onMeasure(widthMeasureSpec, widthMeasureSpec)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (color == Color.TRANSPARENT) {
            if (transparentGrid == null) {
                transparentGrid = getDrawable(
                  context,
                  R.drawable.md_transparent
                )
            }
            transparentGrid?.setBounds(0, 0, dpToPx(rectangleDefault), dpToPx(rectangleDefault))
            transparentGrid?.draw(canvas)
        } else {
            canvas.drawRoundRect(rectF, boundRadiusX, boundRadiusY, fillPaint)
        }

        canvas.drawRoundRect(rectF, boundRadiusX, boundRadiusY, strokePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        transparentGrid = null
    }
}