package com.android.base.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
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

    private var layoutView: View? = null
    private var cachedView: View? = null

    @Volatile
    private var _loadingView: LoadingView? = null
    private val loadingView: LoadingView
        get() {
            synchronized(this) {
                return _loadingView
                    ?: onCreateLoadingView()
                    ?: Sword.get().loadingViewFactory.createLoadingDelegate(context)
            }
        }

    /** Fragment需要自己处理BackPress事件，如果返回false代表fragment不拦截事件，交给子Fragment处理，如果子Fragment也不处理，则由Activity处理。*/
    open fun handleBackPress() = false

    protected open fun onCreateLoadingView(): LoadingView? = null
    protected open fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Any? = null

    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}
    internal open fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {}
    override fun onBackPressed() = handleBackPress() || BackHandlerHelper.handleBackPress(this)

    override fun showLoadingDialog() = loadingView.showLoadingDialog(true)
    override fun showLoadingDialog(cancelable: Boolean) = loadingView.showLoadingDialog(cancelable)
    override fun showLoadingDialog(message: CharSequence, cancelable: Boolean) =
        loadingView.showLoadingDialog(message, cancelable)

    override fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean) =
        loadingView.showLoadingDialog(messageId, cancelable)

    override fun dismissLoadingDialog() = loadingView.dismissLoadingDialog()
    override fun showMessage(message: CharSequence) = loadingView.showMessage(message)
    override fun showMessage(@StringRes messageId: Int) = loadingView.showMessage(messageId)
    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
        Timber.tag(tag())
            .d(">>>>>>> onPrimaryNavigationFragmentChanged: [isPrimaryNavigationFragment= $isPrimaryNavigationFragment]")
    }

    private fun tag() = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(tag()).d(">>>>>>> onAttach: [context= $context]")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(tag()).d(">>>>>>> onCreate: [savedInstanceState= $savedInstanceState]")
    }

    /**
     * 为了避免增加不必要的代码入侵，除了[HomeFragment, DiaryFragment, MineFragment] 以外，都应该重写此方法 [onCreateView]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (cachedView == null) {
            val layout = provideLayout(inflater, container, savedInstanceState) ?: return null
            if (layout is Int) {
                cachedView = inflater.inflate(layout, container, false)
                return cachedView
            }
            if (layout is View) {
                cachedView = layout
                return layout
            }
            throw IllegalArgumentException("You should provide a layout id or a View")
        }

        Timber.tag(tag()).d(">>>>>>> onCreateView.parent: [container= ${cachedView!!.parent}]")

        if (cachedView!!.parent != null) {
            val parent = cachedView!!.parent
            if (parent is ViewGroup) {
                parent.removeView(cachedView)
            }
        }

        return cachedView
    }

    /**
     * 为了避免增加不必要的代码入侵，除了[HomeFragment, DiaryFragment, MineFragment] 以外，都应该重写此方法 [onViewCreated]
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(tag()).d(">>>>>>> onViewCreated: [savedInstanceState= ${savedInstanceState}]")

        if (layoutView !== view) {
            layoutView = view
            internalOnViewPrepared(view, savedInstanceState)
            onViewPrepared(view, savedInstanceState)
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(tag()).d(">>>>>>> onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.tag(tag()).d(">>>>>>> onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(tag()).d(">>>>>>> onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.tag(tag()).d(">>>>>>> onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag(tag()).d(">>>>>>> onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag(tag()).d(">>>>>>> onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.tag(tag()).d(">>>>>>> onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.tag(tag()).d(">>>>>>> onSaveInstanceState: [outState= ${outState}]")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.tag(tag())
            .d(">>>>>>> onViewStateRestored: [savedInstanceState= ${savedInstanceState}]")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.tag(tag()).d(">>>>>>> onHiddenChanged: [hidden= ${hidden}]")
    }
}