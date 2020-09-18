@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING", "RedundantSetter")

package com.android.base.widget.statusLayout

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import com.android.base.R

/**
 * 简单配置缺省页
 *
 * 全局配置
 * 单例配置
 * 支持代码或者布局创建
 * 无网络情况下showLoading显示错误布局, 有网则显示加载中布局
 *
 * @property emptyLayout 空页的layoutRes
 * @property errorLayout 错误页的layoutRes
 * @property loadingLayout 加载页的layoutRes
 * @property status 当前缺省页状态[Status]
 * @property loaded 当前缺省页是否加载成功过, 即是否执行过[showContent]
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var contentId: Int = -2
    private val contentViews = ArrayMap<Int, View>()
    private var retryIds: List<Int>? = null
    private var refresh = true

    private var onEmpty: (View.(Any?) -> Unit)? = null
    private var onError: (View.(Any?) -> Unit)? = null
    private var onLoading: (View.(Any?) -> Unit)? = null
    private var onRefresh: (StateLayout.(View) -> Unit)? = null

    private var stateChanged = false
    private var trigger = false

    var loaded = false

    var status = Status.CONTENT
        private set

    // <editor-fold desc="设置缺省页">

    @LayoutRes
    var errorLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    @LayoutRes
    var emptyLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    @LayoutRes
    var loadingLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    // </editor-fold>


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        try {
            emptyLayout = attributes.getResourceId(R.styleable.StateLayout_sl_empty_layout, NO_ID)
            errorLayout = attributes.getResourceId(R.styleable.StateLayout_sl_error_layout, NO_ID)
            loadingLayout = attributes.getResourceId(R.styleable.StateLayout_sl_loading_layout, NO_ID)
        } finally {
            attributes.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 || childCount == 0) {
            throw UnsupportedOperationException("StateLayout只能包含一个子视图")
        }
        if (contentViews.size == 0) {
            val view = getChildAt(0)
            setContentView(view)
        }
    }

    // <editor-fold desc="生命周期">

    /**
     * 当空缺省页显示时回调
     * @see showEmpty
     * @see StateConfig.onEmpty
     */
    fun onEmpty(block: View.(Any?) -> Unit): StateLayout {
        onEmpty = block
        return this
    }

    /**
     * 当加载中缺省页显示时回调
     * @see showLoading
     * @see StateConfig.onLoading
     */
    fun onLoading(block: View.(Any?) -> Unit): StateLayout {
        onLoading = block
        return this
    }

    /**
     * 当错误缺省页显示时回调
     * @see showError
     * @see StateConfig.onError
     */
    fun onError(block: View.(Any?) -> Unit): StateLayout {
        onError = block
        return this
    }

    /**
     * 当[showLoading]时会回调该函数参数, 一般将网络请求等异步操作放入其中
     */
    fun onRefresh(block: StateLayout.(loading: View) -> Unit): StateLayout {
        onRefresh = block
        return this
    }

    // </editor-fold>


    // <editor-fold desc="显示缺省页">


    /**
     * 有网则显示加载中, 无网络直接显示错误, 会触发[onLoading]的函数参数
     * @param tag 传递的tag将被[onLoading]接收
     * @param refresh 是否调用刷新回调[onRefresh]
     */
    fun showLoading(tag: Any? = null, refresh: Boolean = true) {
        this.refresh = refresh

        if (loadingLayout == NO_ID) {
            loadingLayout = StateConfig.loadingLayout
        }

        if (status == Status.LOADING) {
            if (onLoading == null) {
                StateConfig.onLoading?.let { onLoading = it }
            }
            onLoading?.invoke(getView(loadingLayout), tag)
            return
        }

        if (loadingLayout != NO_ID) {
            show(loadingLayout, tag)
        }
    }

    /**
     * 显示空页, 会触发[onEmpty]的函数参数
     * @param tag 传递的tag将被[onEmpty]接收
     */
    fun showEmpty(tag: Any? = null) {
        if (emptyLayout == NO_ID) {
            emptyLayout = StateConfig.emptyLayout
        }
        if (emptyLayout != NO_ID) {
            show(emptyLayout, tag)
        }
    }

    /**
     * 显示错误页, 会触发[onError]的函数参数
     * @param tag 传递的tag将被[onError]接收
     */
    fun showError(tag: Any? = null) {
        if (errorLayout == NO_ID) {
            errorLayout = StateConfig.errorLayout
        }
        if (errorLayout != NO_ID) {
            show(errorLayout, tag)
        }
    }

    /**
     * 显示内容布局, 表示成功缺省页
     */
    fun showContent() {
        if (trigger && stateChanged) return
        loaded = true
        show(contentId)
    }

    // </editor-fold>

    /**
     * 错误页/空页中的布局控件包含指定IdRes的会设置点击事件, 该点击事件会触发[StateLayout.showLoading]
     * 点击事件500ms内防抖动
     */
    fun setRetryIds(@IdRes vararg ids: Int): StateLayout {
        retryIds = ids.toList()
        return this
    }

    /**
     * 一般情况下开发者无需关心, 这属于配合其他框架预览函数
     */
    fun trigger(): Boolean {
        trigger = !trigger
        if (!trigger) stateChanged = false
        return trigger
    }


    /**
     * 显示视图
     */
    private fun show(layoutId: Int, tag: Any? = null) {

        if (trigger) {
            stateChanged = true
        }

        runMain {

            for (view in contentViews.values) view.visibility = View.GONE

            try {
                val view = getView(layoutId)
                view.visibility = View.VISIBLE

                when (layoutId) {

                    // 空
                    emptyLayout -> {
                        status = Status.EMPTY

                        if (retryIds == null) retryIds = StateConfig.retryIds
                        retryIds?.forEach {
                            view.findViewById<View>(it)?.throttleClick { showLoading() }
                        }

                        if (onEmpty == null) StateConfig.onEmpty?.let { onEmpty = it }

                        onEmpty?.invoke(view, tag)
                    }

                    // 错误
                    errorLayout -> {
                        status = Status.ERROR

                        if (retryIds == null) retryIds = StateConfig.retryIds
                        retryIds?.forEach {
                            view.findViewById<View>(it)?.throttleClick { showLoading() }
                        }

                        if (onError == null) StateConfig.onError?.let { onError = it }

                        onError?.invoke(view, tag)
                    }

                    // 加载中
                    loadingLayout -> {
                        status = Status.LOADING

                        if (onLoading == null) {
                            StateConfig.onLoading?.let { onLoading = it }
                        }

                        onLoading?.invoke(view, tag)

                        if (refresh) onRefresh?.invoke(this, view)
                    }

                    // 内容
                    else -> status = Status.CONTENT
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun remove(layoutId: Int?) {
        if (contentViews.containsKey(layoutId)) {
            val stateView = contentViews.remove(layoutId)
            removeView(stateView)
        }
    }

    @Throws(NullPointerException::class)
    private fun getView(@LayoutRes layoutId: Int): View {
        if (contentViews.containsKey(layoutId)) {
            return contentViews[layoutId]!!
        }
        val view = LayoutInflater.from(context).inflate(layoutId, this, false)
        addView(view)

        contentViews[layoutId] = view
        return view
    }


    /**
     * 标记视图为内容布局, 用于扩展替换内容. 一般情况不需要使用
     */
    fun setContentView(view: View) {
        contentViews[contentId] = view
    }

    /**
     * 判断是否有网络连接
     */
    @SuppressLint("MissingPermission")
    fun Context?.isNetConnected(): Boolean {
        if (this != null) {
            val mConnectivityManager = getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                @Suppress("DEPRECATION")
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    private fun runMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            Handler(Looper.getMainLooper()).post { block() }
        }
    }
}

