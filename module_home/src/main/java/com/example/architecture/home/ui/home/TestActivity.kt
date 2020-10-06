package com.example.architecture.home.ui.home

import android.app.SharedElementCallback
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commitNow
import com.android.base.utils.android.views.newFragment
import com.app.base.app.AppBaseActivity
import com.bumptech.glide.Glide
import com.example.architecture.home.R
import com.example.architecture.home.ui.allgames.AllGamesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.test.*

@AndroidEntryPoint
class TestActivity : AppBaseActivity() {

    private val allGamesFragment by lazy { newFragment<AllGamesFragment>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.test)

        supportFragmentManager.commitNow {
            replace(R.id.common_container_id, allGamesFragment, AllGamesFragment::class.java.name)
            setPrimaryNavigationFragment(allGamesFragment)
        }
    }
}