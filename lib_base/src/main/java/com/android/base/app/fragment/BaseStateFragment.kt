package com.android.base.app.fragment

import android.view.View
import androidx.annotation.NonNull
import com.android.base.app.ui.RefreshStateLayout
import com.android.base.app.ui.RefreshView
import com.android.base.app.ui.StateLayoutConfig
import android.os.Bundle


/**
 * <pre>
 *    1: 支持显示{CONTENT、LOADING、ERROR、EMPTY}四种布局、支持下拉刷新
 *    2: RefreshView(下拉刷新)的id必须设置为 ：R.id.base_refresh_layout，没有添加则表示不需要下拉刷新功能
 *    3: 默认所有重试和下拉刷新都会调用 {@link #onRefresh()}，子类可以修改该行为
 *    4: 有关自定义MultiStateLayout请参考 {@link com.android.base.widget.SimpleMultiStateLayout}
 * </pre>
 */
abstract class BaseStateFragment : BaseFragment(), RefreshStateLayout {

    private lateinit var stateLayout: RefreshStateLayoutImpl

    /** How to use these :
     *
     *
     * stateLayoutConfig.setStateIcon(EMPTY, R.drawable.xxx)
     *                  .setStateAction(EMPTY, "")
     *                  .setStateMessage(EMPTY, getString(R.string.xxx))
     *                  .setMessageGravity(EMPTY, "")
     *                  .setStateRetryListener{}
     */
    @JvmField protected val CONTENT = StateLayoutConfig.CONTENT
    @JvmField protected val LOADING = StateLayoutConfig.LOADING
    @JvmField protected val ERROR = StateLayoutConfig.ERROR
    @JvmField protected val EMPTY = StateLayoutConfig.EMPTY
    @JvmField protected val NET_ERROR = StateLayoutConfig.NET_ERROR
    @JvmField protected val SERVER_ERROR = StateLayoutConfig.SERVER_ERROR

    protected open fun onRefresh() {}
    protected open fun canRefresh(): Boolean = true

    override fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {
        setupRefreshStateLayoutOnViewCreated(view)
    }

    /**
     * 如果你的Fragment重写了 [BaseFragment.onViewCreated] 方法，那么请主动调用该方法初始化 RefreshStateLayout
     */
    protected fun setupRefreshStateLayoutOnViewCreated(@NonNull view: View) {
        stateLayout = RefreshStateLayoutImpl.init(view)
        stateLayout.setRefreshHandler(object : RefreshView.RefreshHandler() {
            override fun onRefresh() {
                this@BaseStateFragment.onRefresh()
            }

            override fun canRefresh() = this@BaseStateFragment.canRefresh()
        })

        stateLayout.setStateRetryListenerUnchecked {
            getRefreshView()?.let {
                if (!it.isRefreshing) {
                    autoRefresh()
                }
            } ?: onRefresh()
        }
    }

    override fun isRefreshing(): Boolean = getRefreshView() != null && getRefreshView()!!.isRefreshing;

    internal fun getRefreshView(): RefreshView? {
        return stateLayout.refreshView
    }

    private fun getStateLayout(): RefreshStateLayout {
        return stateLayout
    }

    @NonNull
    override fun getStateLayoutConfig(): StateLayoutConfig {
        return stateLayout.stateLayoutConfig
    }


    // --------------------------------------------------------------- impl ---------------------------------------------------------------


    override fun refreshCompleted() {
        getStateLayout().refreshCompleted()
    }

    override fun autoRefresh() {
        getStateLayout().autoRefresh()
    }

    override fun showContentLayout() {
        getStateLayout().showContentLayout()
    }

    override fun showLoadingLayout() {
        getStateLayout().showLoadingLayout()
    }

    override fun showEmptyLayout() {
        getStateLayout().showEmptyLayout()
    }

    override fun showErrorLayout() {
        getStateLayout().showErrorLayout()
    }

    override fun showRequesting() {
        getStateLayout().showRequesting()
    }

    override fun showBlank() {
        getStateLayout().showBlank()
    }

    override fun showNetErrorLayout() {
        getStateLayout().showNetErrorLayout()
    }

    override fun showServerErrorLayout() {
        getStateLayout().showServerErrorLayout()
    }

    override fun currentStatus(): Int {
        return getStateLayout().currentStatus()
    }
}