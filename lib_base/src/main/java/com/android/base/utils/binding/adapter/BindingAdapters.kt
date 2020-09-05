/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.android.base.utils.binding.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.TransitionManager
import com.android.base.imageloader.DisplayConfig
import com.android.base.imageloader.GlideImageLoader
import com.android.base.imageloader.Source
import com.android.base.rx.observeOnUI
import com.android.base.rx.subscribeIgnoreError
import com.android.base.utils.ktx.no
import com.android.base.utils.ktx.yes
import com.blankj.utilcode.util.VibrateUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

object ImageViewBindingAdapter {

    /** imageView 通过 url 获取图片、设置占位图、圆形、圆角
     * @param url 路径
     * @param placeholderRes 占位图
     * @param roundCorners 倒角
     */
    @JvmStatic
    @BindingAdapter(
        value = ["android:src", "android:placeholderRes", "android:roundCorners"],
        requireAll = false
    )
    fun setImageUrl(view: ImageView, url: String?, placeholderRes: Int?, roundCorners: Int?) {
        if (url.isNullOrBlank()) {
            if (placeholderRes != null && placeholderRes != 0) {
                view.setImageResource(placeholderRes)
            } else {
                view.setImageResource(0)
            }
        } else {
            val config = DisplayConfig.create()
            if (placeholderRes != null && placeholderRes > 0) {
                config.setLoadingPlaceholder(placeholderRes)
                config.setErrorPlaceholder(placeholderRes)
            }
            if (roundCorners != null && roundCorners > 0) {
                config.setRoundedCornersRadius(roundCorners)
            }
            GlideImageLoader().display(view, url, config)
        }
    }

    /** imageView 通过 uri 获取图片、设置占位图、圆形、圆角
     * @param uri 路径
     * @param placeholderRes 占位图
     * @param roundCorners 倒角*/
    @JvmStatic
    @BindingAdapter(
        value = ["android:src", "android:placeholderRes", "android:roundCorners"],
        requireAll = false
    )
    fun setImageUri(view: ImageView, uri: Uri?, placeholderRes: Int?, roundCorners: Int?) {
        uri ?: return
        val config = DisplayConfig.create()
        if (placeholderRes != null && placeholderRes > 0) {
            config.setLoadingPlaceholder(placeholderRes)
            config.setErrorPlaceholder(placeholderRes)
        }
        if (roundCorners != null && roundCorners > 0) {
            config.setRoundedCornersRadius(roundCorners)
        }
        GlideImageLoader().display(view, Source.createWithUri(uri), config)
    }
}

object AppBarLayoutBindingAdapter {

    /** 设置appBarLayout 是否可以滑动 */
    @JvmStatic
    @BindingAdapter(value = ["android:isScroll"], requireAll = false)
    fun setAppBarLayoutScroll(view: AppBarLayout, isScroll: Boolean) {
        val layoutParams: CoordinatorLayout.LayoutParams =
            view.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        behavior?.apply {
            this as AppBarLayout.Behavior
            setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return isScroll
                }
            })
        }
    }
}

object CompoundButtonAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["android:onCheckedChanged", "android:checkedAttrChanged"],
        requireAll = false
    )
    fun setListeners(
        view: CompoundButton, listener: CompoundButton.OnCheckedChangeListener?,
        attrChange: InverseBindingListener?
    ) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener)
        } else {
            view.setOnCheckedChangeListener { buttonView, isChecked ->
                listener?.onCheckedChanged(buttonView, isChecked)
                attrChange.onChange()
            }
        }
    }
}

object ViewAdapter {

    /** 动态修改占的比例 */
    @JvmStatic
    @BindingAdapter(value = ["app:layout_constraintHorizontal_weight"])
    fun setHorizontalWeight(view: View, weight: Int) {
        if (weight > 0) {
            try {
                val constraintSet = ConstraintSet()
                val constraintLayout = view.parent as ConstraintLayout
                constraintSet.clone(constraintLayout)
                constraintSet.setHorizontalWeight(view.id, weight.toFloat())
                TransitionManager.beginDelayedTransition(constraintLayout)
                constraintSet.applyTo(constraintLayout)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:onTouch"])
    fun setOnTouchListener(view: View, onTouchListener: View.OnTouchListener) {
        view.setOnTouchListener(onTouchListener)
    }

}

/*object LottieAnimationViewBindingAdapter {
    *//** LottieAnimationView 加载raw 中的json文件 *//*
    @JvmStatic
    @BindingAdapter("app:lottie_file")
    fun setLottieFile(view: LottieAnimationView, @RawRes resId: Int) {
        if (resId == 0) {
            return
        }
        if (view.tag == resId.toString()) {
            view.playAnimation()
        } else {
            view.cancelAnimation()
            view.setAnimation(resId)
            view.tag = resId.toString()
            view.playAnimation()
        }
    }
}*/

object RadioGroupBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["android:onCheckedChanged", "android:checkedButtonAttrChanged"],
        requireAll = false
    )
    fun setListeners(
        view: RadioGroup, listener: RadioGroup.OnCheckedChangeListener?,
        attrChange: InverseBindingListener?
    ) {
        if (attrChange == null) {
            view.setOnCheckedChangeListener(listener)
        } else {
            view.setOnCheckedChangeListener { group, checkedId ->
                listener?.onCheckedChanged(group, checkedId)
                attrChange.onChange()
            }
        }
    }
}

object RefreshViewBindingAdapter {

    /** 是否可以刷新 */
    @JvmStatic
    @BindingAdapter("app:isCanRefresh")
    fun isSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout, isRefresh: Boolean) {
        swipeRefreshLayout.isEnabled = isRefresh
    }

    /** 设置正在刷新 */
    @JvmStatic
    @BindingAdapter("app:isRefreshing")
    fun refreshing(swipeRefreshLayout: SwipeRefreshLayout, isRefresh: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefresh
    }
}

object WebViewAdapter {

    /** webView 加载Html string类型
     * @param html 渲染的 HTML 网页 */
    @JvmStatic
    @BindingAdapter("app:render")
    fun loadHtml(webView: WebView?, html: String) {
        html.isEmpty().no {
            webView?.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        }
    }

    /** webView 通过URL加载，但是必须要传BaseUrl
     * @param baseUrl 网页地址 baseUrl
     * @param data 链接地址 */
    @JvmStatic
    @BindingAdapter(value = ["app:baseUrl", "app:data"])
    fun loadHtmlData(webView: WebView?, baseUrl: String, data: String) {
        if (baseUrl.isNotEmpty() && data.isNotEmpty()) {
            webView?.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null)
        }
    }
}

object ViewBindAdapter {

    @JvmStatic
    @BindingAdapter("android:onFocusChange")
    fun setOnFocusChangeListener(view: EditText, listener: View.OnFocusChangeListener) {
        view.onFocusChangeListener = listener
    }

    /** View设置选中状态 */
    @JvmStatic
    @BindingAdapter("android:selected")
    fun setSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    @BindingAdapter(
        "android:onClick",
        "android:isThrottleFirst",
        "android:isVibrate",
        requireAll = false
    )
    fun setOnClick(
        view: View, clickListener: View.OnClickListener?,
        isThrottleFirst: Boolean?, isVibrate: Boolean?
    ) {
        view.isClickable = true
        clickListener ?: return
        if (isThrottleFirst != null) {
            RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOnUI()
                .subscribeIgnoreError {
                    clickListener.onClick(view)
                    isVibrate?.yes { VibrateUtils.vibrate(longArrayOf(15, 15), -1) }
                }
        } else {
            view.setOnClickListener {
                clickListener.onClick(it)
                isVibrate?.yes { VibrateUtils.vibrate(longArrayOf(15, 15), -1) }
            }
        }
    }
}
