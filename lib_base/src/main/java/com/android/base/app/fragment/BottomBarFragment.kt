package com.android.base.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.TagsFactory
import com.android.base.app.activity.OnBackPressListener
import com.android.base.app.ui.LoadingView
import timber.log.Timber

/**
 * 切换页面时不会销毁，缓存的 fragment。
 */
open class BottomBarFragment : BaseFragment(), LoadingView, OnBackPressListener {

    private var layoutView: View? = null
    private var cachedView: View? = null

    protected open fun provideLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Any? = null

    protected open fun onViewPrepared(view: View, savedInstanceState: Bundle?) {}
    internal open fun internalOnViewPrepared(view: View, savedInstanceState: Bundle?) {}

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

        Timber.tag(TagsFactory.fragment_lifecycle)
            .i(">>>>>>> onCreateView.parent: [container= ${cachedView!!.parent}]")

        if (cachedView!!.parent != null) {
            val parent = cachedView!!.parent
            if (parent is ViewGroup) {
                parent.removeView(cachedView)
            }
        }

        return cachedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TagsFactory.fragment_lifecycle)
            .i(">>>>>>> onViewCreated: [savedInstanceState= ${savedInstanceState}]")

        if (layoutView !== view) {
            layoutView = view
            internalOnViewPrepared(view, savedInstanceState)
            onViewPrepared(view, savedInstanceState)
        }
    }

}