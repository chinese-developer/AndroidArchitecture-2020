package com.android.base.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.android.base.TagsFactory
import com.android.base.app.Sword
import com.android.base.app.activity.BackHandlerHelper
import com.android.base.app.activity.OnBackPressListener
import com.android.base.app.ui.LoadingView
import timber.log.Timber

/**
 * 提供：
 *
 * 1. 返回键监听
 * 2. 显示 LoadingDialog 和 Message
 * 3. 可以添加生命周期代理
 * 4. RxJava 生命周期绑定
 */
open class BaseFragment : Fragment(), LoadingView, OnBackPressListener {

    private val loadingView by lazy { Sword.get().loadingViewFactory.createLoadingDelegate(context) }

    /** 返回 true，则代表 Fragment 需要自己处理 BackPress 事件，如果返回 false 代表 Fragment 不拦截事件，交给子 Fragment 处理，如果子 Fragment 也不处理，则由 Activity 处理。*/
    open fun handleBackPress() = false
    override fun onBackPressed() = handleBackPress() || BackHandlerHelper.handleBackPress(this)

    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
        Timber.tag(TagsFactory.fragment_lifecycle)
            .i(">>>>>>> onPrimaryNavigationFragmentChanged: [isPrimaryNavigationFragment= $isPrimaryNavigationFragment]")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onAttach: [context= $context]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onCreate ${ if (savedInstanceState != null) ": [savedInstanceState= $savedInstanceState]" else ""}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onCreateView ")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onViewCreated ${ if (savedInstanceState != null) ": [savedInstanceState= $savedInstanceState]" else ""}")
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onSaveInstanceState: [outState= ${outState}]")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.tag(TagsFactory.fragment_lifecycle)
            .i(">>>>>>> onViewStateRestored: [savedInstanceState= ${savedInstanceState}]")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.tag(TagsFactory.fragment_lifecycle).i(">>>>>>> onHiddenChanged: [hidden= ${hidden}]")
    }

    override fun showLoadingDialog() = loadingView.showLoadingDialog(true)
    override fun showLoadingDialog(cancelable: Boolean) = loadingView.showLoadingDialog(cancelable)
    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean) =
        loadingView.showLoadingDialog(message, cancelable)

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean) =
        loadingView.showLoadingDialog(messageId, cancelable)

    override fun showMessage(message: CharSequence) = loadingView.showMessage(message)
    override fun showMessage(@StringRes messageId: Int) = loadingView.showMessage(messageId)

    override fun dismissLoadingDialog() = loadingView.dismissLoadingDialog()

}