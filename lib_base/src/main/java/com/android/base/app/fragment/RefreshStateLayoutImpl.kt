package com.android.base.app.fragment

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import com.android.base.app.ui.*
import com.android.base.app.ui.CommonId.REFRESH_ID
import com.android.base.app.ui.CommonId.STATE_ID
import com.android.base.widget.StateProcessor

internal class RefreshStateLayoutImpl private constructor(layoutView: View) : RefreshStateLayout, StateLayoutConfig {

    lateinit var refreshView: RefreshView
        private set

    private lateinit var multiStateView: StateLayout
    private var refreshHandler: RefreshView.RefreshHandler? = null

    init {
        setupBaseUiLogic(layoutView)
        setupRefreshLogic(layoutView)
    }

    fun setRefreshHandler(refreshHandler: RefreshView.RefreshHandler) {
        this.refreshHandler = refreshHandler
    }

    fun setStateRetryListenerUnchecked(retryActionListener: (Any) -> Unit) {
        setStateRetryListener(retryActionListener)
    }

    private fun setupBaseUiLogic(layoutView: View) {
        multiStateView = layoutView.findViewById<View>(STATE_ID) as StateLayout
    }

    private fun setupRefreshLogic(layoutView: View) {
        val refreshLayout = layoutView.findViewById<View>(REFRESH_ID) ?: return
        refreshView = RefreshViewFactory.createRefreshView(refreshLayout)
        refreshView.setRefreshHandler(object : RefreshView.RefreshHandler() {
            override fun canRefresh(): Boolean {
                return refreshHandler!!.canRefresh()
            }

            override fun onRefresh() {
                refreshHandler!!.onRefresh()
            }
        })
    }

    override fun autoRefresh() {
        refreshView.autoRefresh()
    }

    override fun showLoadingLayout() {
        checkMultiStateView().showLoadingLayout()
    }

    override fun showContentLayout() {
        refreshCompleted()
        checkMultiStateView().showContentLayout()
    }

    override fun showEmptyLayout() {
        refreshCompleted()
        checkMultiStateView().showEmptyLayout()
    }

    override fun showErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showErrorLayout()
    }

    override fun showRequesting() {
        checkMultiStateView().showRequesting()
    }

    override fun showBlank() {
        checkMultiStateView().showBlank()
    }

    override fun showNetErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showNetErrorLayout()
    }

    override fun showServerErrorLayout() {
        refreshCompleted()
        checkMultiStateView().showServerErrorLayout()
    }

    override fun getStateLayoutConfig(): StateLayoutConfig {
        checkMultiStateView()
        return this
    }

    override fun currentStatus(): Int {
        return multiStateView.currentStatus()
    }

    override fun refreshCompleted() {
        refreshView.refreshCompleted()
    }

    override fun isRefreshing(): Boolean {
        return refreshView.isRefreshing
    }

    override fun setStateMessage(@StateLayoutConfig.RetryableState state: Int, message: CharSequence): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateMessage(state, message)
        return this
    }

    override fun setMessageGravity(state: Int, gravity: Int): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setMessageGravity(state, gravity)
        return this
    }

    override fun setStateIcon(@StateLayoutConfig.RetryableState state: Int, drawable: Drawable): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawable)
        return this
    }

    override fun setStateIcon(@StateLayoutConfig.RetryableState state: Int, @DrawableRes drawableId: Int): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateIcon(state, drawableId)
        return this
    }

    override fun setStateAction(@StateLayoutConfig.RetryableState state: Int, actionText: CharSequence): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateAction(state, actionText)
        return this
    }

    override fun setStateRetryListener(retryActionListener: OnRetryActionListener): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.setStateRetryListener(retryActionListener)
        return this
    }

    override fun disableOperationWhenRequesting(disable: Boolean): StateLayoutConfig {
        checkMultiStateView().stateLayoutConfig.disableOperationWhenRequesting(disable)
        return this
    }

    override fun getProcessor(): StateProcessor {
        return checkMultiStateView().stateLayoutConfig.processor
    }

    private fun checkMultiStateView(): StateLayout {
        return multiStateView
    }

    companion object {

        fun init(layoutView: View): RefreshStateLayoutImpl {
            return RefreshStateLayoutImpl(layoutView)
        }
    }

}
