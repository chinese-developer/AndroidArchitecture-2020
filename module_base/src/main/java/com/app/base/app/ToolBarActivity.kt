package com.app.base.app

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.app.base.R

abstract class ToolBarActivity : AppBaseActivity() {

    protected lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        mToolbar = findViewById(R.id.common_toolbar)

        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            mToolbar.contentInsetStartWithNavigation = 0
            mToolbar.setNavigationOnClickListener { _ -> onNavigationClick() }
        }
    }

    protected fun setToolbarTitle(title: CharSequence) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    protected fun setToolbarTitle(@StringRes titleRes: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(titleRes)
        }
    }

    protected fun onNavigationClick() {
        onBackPressed()
    }

}