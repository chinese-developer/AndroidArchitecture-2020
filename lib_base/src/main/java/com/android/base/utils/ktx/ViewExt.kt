package com.android.base.utils.ktx

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.android.base.rx.subscribeIgnoreError
import com.android.base.utils.android.WindowUtils
import com.android.base.utils.android.compat.AndroidVersion.atLeast
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isInvisible() = this.visibility == View.INVISIBLE
fun View.isGone() = this.visibility == View.GONE

val View.realContext: FragmentActivity?
    get() {
        var context = context
        while (context is android.content.ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

fun View.setClickFeedback(pressAlpha: Float = 0.5F) {
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.alpha = pressAlpha
            }
            MotionEvent.ACTION_UP -> {
                v.alpha = 1F
            }
            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1F
            }
        }
        false
    }
}

inline fun View.doOnLayoutAvailable(crossinline block: () -> Unit) {
    //isLaidOut 方法作用：如果 view 已经通过至少一个布局，则返回true，因为它最后一次附加到窗口或从窗口分离。
    ViewCompat.isLaidOut(this).yes {
        block()
    }.otherwise {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                removeOnLayoutChangeListener(this)
                block()
            }
        })
    }
}

inline fun <T : View> T.onGlobalLayoutOnce(crossinline action: T.() -> Unit) {
    val t: T = this
    t.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    action.invoke(t)
                    if (atLeast(16)) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        @Suppress("DEPRECATION")
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            })
}

fun View.setPaddingAll(padding: Int) {
    this.setPadding(padding, padding, padding, padding)
}

fun View.setPaddingLeft(padding: Int) {
    this.setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setPaddRight(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setPaddingTop(padding: Int) {
    this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.setPaddingBottom(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun newLayoutParams(width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT, height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(width, height)
}

fun newMarginLayoutParams(width: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT, height: Int = ViewGroup.MarginLayoutParams.WRAP_CONTENT): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(width, height)
}

fun View.setTopMargin(topMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = topMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.topMargin = topMargin
        }
    }
}

fun View.setBottomMargin(bottomMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.bottomMargin = bottomMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.bottomMargin = bottomMargin
        }
    }
}

fun View.setLeftMargin(leftMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.leftMargin = leftMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.leftMargin = leftMargin
        }
    }
}

fun View.setRightMargin(rightMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.rightMargin = rightMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.rightMargin = rightMargin
        }
    }
}

fun View.setRightMargins(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
    val params: ViewGroup.LayoutParams? = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.rightMargin = rightMargin
        params.leftMargin = leftMargin
        params.bottomMargin = bottomMargin
        params.topMargin = topMargin
    } else {
        layoutParams = newMarginLayoutParams().apply {
            this.rightMargin = rightMargin
            this.leftMargin = leftMargin
            this.bottomMargin = bottomMargin
            this.topMargin = topMargin
        }
    }
}

fun View.setWidth(width: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.height = height
    layoutParams = params
}

fun View.setSize(width: Int, height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    params.height = height
    layoutParams = params
}

fun View.onDebouncedClick(onClick: (View) -> Unit) {
    onClickObservable(500)
            .subscribeIgnoreError { onClick(this) }
}

fun View.onDebouncedClick(milliseconds: Long, onClick: (View) -> Unit) {
    onClickObservable(milliseconds)
            .subscribeIgnoreError { onClick(this) }
}

fun View.onClickObservable(): Observable<Unit> {
    return onClickObservable(500)
}

fun View.onClickObservable(milliseconds: Long): Observable<Unit> {
    return clicks()
            .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
}

inline val ViewGroup.views get() = (0 until childCount).map { getChildAt(it) }

fun View.measureSelf(): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
        return false
    }
    val size = 1 shl 30 - 1//即后30位
    val measureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.AT_MOST)
    measure(measureSpec, measureSpec)
    return true
}

fun View.measureSelfWithScreenSize(): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
        return false
    }
    measure(
            View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(), View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenHeight(), View.MeasureSpec.AT_MOST))
    return true
}

fun View.measureSelf(width: Int, height: Int): Boolean {
    val layoutParams = layoutParams
    if (layoutParams == null || layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
        return false
    }
    measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
    return true
}

fun View.setBgDrawable(drawable: Drawable) {
    background = drawable
}

@Suppress("UNCHECKED_CAST")
fun <V : View> View.find(@IdRes viewId: Int): V {
    return findViewById<View>(viewId) as V
}

private const val ACTION_VISIBLE = 0x01
private const val ACTION_GONE = 0x02
private const val ACTION_INVISIBLE = 0x03
private const val ACTION_DISABLE = 0x04
private const val ACTION_ENABLE = 0x05

fun disable(view1: View, view2: View) {
    view1.isEnabled = false
    view2.isEnabled = false
}

fun disable(view1: View, view2: View, view3: View) {
    view1.isEnabled = false
    view2.isEnabled = false
    view3.isEnabled = false
}

fun disable(view1: View, view2: View, view3: View, vararg views: View) {
    view1.isEnabled = false
    view2.isEnabled = false
    view3.isEnabled = false
    doAction(ACTION_DISABLE, *views)
}

fun enable(view1: View, view2: View) {
    view1.isEnabled = true
    view2.isEnabled = true
}

fun enable(view1: View, view2: View, view3: View) {
    view1.isEnabled = true
    view2.isEnabled = true
    view3.isEnabled = true
}

fun enable(view1: View, view2: View, view3: View, vararg views: View) {
    view1.isEnabled = true
    view2.isEnabled = true
    view3.isEnabled = true
    doAction(ACTION_ENABLE, *views)
}

fun gone(view1: View, view2: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
}

fun gone(view1: View, view2: View, view3: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
    view3.visibility = View.GONE
}

fun gone(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.GONE
    view2.visibility = View.GONE
    view3.visibility = View.GONE
    doAction(ACTION_GONE, *views)
}

fun visible(view1: View, view2: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
}

fun visible(view1: View, view2: View, view3: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
    view3.visibility = View.VISIBLE
}

fun visible(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.VISIBLE
    view2.visibility = View.VISIBLE
    view3.visibility = View.VISIBLE
    doAction(ACTION_VISIBLE, *views)
}

fun invisible(view1: View, view2: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
}

fun invisible(view1: View, view2: View, view3: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
    view3.visibility = View.INVISIBLE
}

fun invisible(view1: View, view2: View, view3: View, vararg views: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.INVISIBLE
    view3.visibility = View.INVISIBLE
    doAction(ACTION_INVISIBLE, *views)
}

private fun doAction(action: Int, vararg views: View) {
    for (view in views) {
        when (action) {
            ACTION_GONE -> view.visibility = View.GONE
            ACTION_INVISIBLE -> view.visibility = View.INVISIBLE
            ACTION_VISIBLE -> view.visibility = View.VISIBLE
            ACTION_ENABLE -> view.isEnabled = true
            ACTION_DISABLE -> view.isEnabled = false
        }
    }
}

inline fun < T: View> T.afterMeasure(crossinline action: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                action()
            }
        }
    })
}
