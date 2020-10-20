package com.example.architecture.home.ui.home

import android.os.Bundle
import androidx.fragment.app.commitNow
import com.android.base.utils.ktx.newFragment
import com.app.base.AppContext
import com.app.base.app.AppBaseActivity
import com.app.base.router.RouterPath
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

        login.setOnClickListener {
            val url = "https://m.ustest01.com/rule?id=58&xgApp=1"
            AppContext.get().appRouter.build(RouterPath.X5WebView.PATH)
                .withString(RouterPath.X5WebView.URL, url)
                .withString(RouterPath.X5WebView.TITLE, "测试标题").navigation()
        }

        supportFragmentManager.commitNow {
            replace(R.id.common_container_id, allGamesFragment, AllGamesFragment::class.java.name)
            setPrimaryNavigationFragment(allGamesFragment)
        }
    }
}