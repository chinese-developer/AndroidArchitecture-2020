/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.provider.Browser
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.R

/** 隐藏软键盘 */
fun MaterialDialog.hideKeyboard() {
  val imm =
    windowContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  val currentFocus = currentFocus
  if (currentFocus != null) {
    currentFocus.windowToken
  } else {
    view.windowToken
  }.let {
    imm.hideSoftInputFromWindow(it, 0)
  }
}

fun Context.startActivityForUriIntent(
  url: String,
  fallback: ((String) -> Unit)? = null
) {
  if (TextUtils.isEmpty(url)) {
    fallback?.invoke("$url is not available And uou must specify a URL")
    return
  }
  Intent(Intent.ACTION_VIEW, Uri.parse(url)).run {
    this.putExtra(Browser.EXTRA_APPLICATION_ID, packageName)
    try {
      startActivity(this)
    } catch (e: ActivityNotFoundException) {
      fallback?.invoke("Activity was not found for intent, $this")
    }
  }
}

fun animateValues(
  from: Int,
  to: Int,
  duration: Long,
  onUpdate: (currentValue: Int) -> Unit,
  onEnd: () -> Unit = {}
): Animator {
  return ValueAnimator.ofInt(from, to)
      .apply {
        this.interpolator = DecelerateInterpolator()
        this.duration = duration
        addUpdateListener {
          onUpdate(it.animatedValue as Int)
        }
        addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) = onEnd()
        })
      }
}

fun createColorSelector(
        context: Context,
        @ColorInt checked: Int = 0,
        @ColorInt unchecked: Int = 0
): ColorStateList {
    val checkedColor = if (checked == 0) MDUtil.resolveColor(
            context = context, attr = R.attr.colorControlActivated
    ) else checked
    return ColorStateList(
            arrayOf(
                    intArrayOf(-android.R.attr.state_checked, -android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_focused)
            ),
            intArrayOf(
                    if (unchecked == 0) MDUtil.resolveColor(
                            context = context, attr = R.attr.colorControlNormal
                    ) else unchecked,
                    checkedColor,
                    checkedColor
            )
    )
}

/**
 * 设置背景shape  全部倒角
 */
fun <T : View> T.setBackgroundRadiusShape(
        radius: Float? = null,
        radii: FloatArray? = null, @ColorInt strokeColor: Int = 0,
        @ColorRes strokeColorResId: Int = 0,
        strokeWidth: Float = 0f
) {
    this.background?.let {
        val gradientDrawable: GradientDrawable
        when (it) {
            is GradientDrawable -> gradientDrawable = it
            is ColorDrawable -> {
                gradientDrawable = GradientDrawable()
                gradientDrawable.setColor(it.color)
            }
            else -> {
                return@let
            }
        }
        radius?.apply {
            gradientDrawable.cornerRadius = this
        }
        radii?.apply {
            gradientDrawable.cornerRadii = this
        }
        if (strokeWidth != 0f) {
            gradientDrawable.setStroke(
                    strokeWidth.toInt(), if (strokeColor == 0) ContextCompat.getColor(context, strokeColorResId) else strokeColor
            )
        }
        setBackgroundResource(0)
        background = gradientDrawable
    }
}

fun <T : View> T.isVisibleForMd(): Boolean {
    return if (this is Button) {
        this.visibility == View.VISIBLE && this.text.trim().isNotBlank()
    } else {
        this.visibility == View.VISIBLE
    }
}

fun <T : View> T.onDetach(onAttached: T.() -> Unit) {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        @Suppress("UNCHECKED_CAST")
        override fun onViewDetachedFromWindow(v: View?) {
            removeOnAttachStateChangeListener(this)
            (v as T).onAttached()
        }

        override fun onViewAttachedToWindow(v: View?) = Unit

    })
}
fun <T : View> T.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun ViewPager.onPageSelected(selection: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) = selection(position)
    })
}

@Suppress("UNCHECKED_CAST")
internal fun <T> MaterialDialog.inflate(
        @LayoutRes res: Int,
        root: ViewGroup? = null
) = LayoutInflater.from(windowContext).inflate(res, root, false) as T

@Suppress("UNCHECKED_CAST")
fun <R : View> ViewGroup.inflate(
        ctxt: Context = context,
        @LayoutRes res: Int
) = LayoutInflater.from(ctxt).inflate(res, this, false) as R

@Suppress("UNCHECKED_CAST")
fun <T> ViewGroup.inflate(
        @LayoutRes res: Int,
        root: ViewGroup? = this
) = LayoutInflater.from(context).inflate(res, root, false) as T


fun <T : View> T.isNotVisible(): Boolean = !isVisibleForMd()
