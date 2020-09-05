package com.app.base.widget.statusbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.compat.StatusBarUtil
import com.app.base.R

class InsetsConstraintLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mStatusRect = Rect()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }

    /**
     * dispatchDraw()主要是分发给子组件进行绘制，我们通常定制组件的时候重写的是onDraw()方法。
     * 值得注意的是ViewGroup容器组件的绘制，当它没有背景时直接调用的是dispatchDraw()方法，而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用。
     * 因此要在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，或者自定制一个Drawable，
     * 重写它的draw(Canvas c)和 getIntrinsicWidth()。
     *
     * 值得注意的是，ViewGroup类已经为我们重写了dispatchDraw ()的功能实现，应用程序一般不需要重写该方法，但可以重载父类、
     */
    override fun dispatchDraw(canvas: Canvas) {
        if (AndroidVersion.atLeast(19)) {
            // 当SdkVersion>=19（Android 4.4）时， fitsSystemWindows只作用在Android4.4及以上的系统，因为4.4以下的系统StatusBar没有透明状态。
            // 所有View都可以设置这个属性，但是添加了这个属性后，padding就会失效。
            // 作用：
            // 设置了android:fitsSystemWindows=”true”属性后针对透明的状态栏会自动添加一个值等于状态栏高度的paddingTop；
            // 针对透明的系统导航栏（底部）会自动添加一个值等于导航栏高度的paddingBottom
            // 这样我们的标题栏就可以正常的显示在statusBar下面了，而不是重叠。
            if (fitsSystemWindows) {
                mStatusRect.set(0, 0, measuredWidth, StatusBarUtil.getStatusBarHeight(context))
                canvas.drawRect(mStatusRect, mPaint)
            }
        }
        super.dispatchDraw(canvas)
    }

}