/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.ktx.getStringArray
import com.android.base.utils.ktx.newFragment
import com.android.base.utils.ktx.onDebouncedClick
import com.android.base.utils.ktx.visible
import com.app.base.AppContext
import com.app.base.data.DataConfig
import com.app.base.debug.isOpenDebug
import com.app.base.router.RouterPath
import com.app.base.utils.DebugModeUtils
import com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC
import com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem.SHAPE_OVAL
import com.example.architecture.home.common.Constant.TAB_1
import com.example.architecture.home.common.Constant.TAB_2
import com.example.architecture.home.common.Constant.TAB_3
import com.example.architecture.home.common.OnTabSelectedListenerAdapter
import com.example.architecture.home.databinding.FragMainBinding
import com.example.architecture.home.ui.home.HomeFragment
import com.example.architecture.home.ui.mine.MineFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private val tabsArr = getStringArray(R.array.main_tabs)

    private lateinit var binding: FragMainBinding
    private lateinit var shapeBadgeItem: ShapeBadgeItem
    private lateinit var homeFragment: HomeFragment
    private lateinit var allGamesFragment: MineFragment
    private lateinit var mediaController: MediaController

    private val model by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaController = MediaController(this, binding, model)
        initViewModelObservers()
        initViewPager()
        initBottomNavigationBar()
        listeners()
    }

    private fun initViewModelObservers() {
        model.songModel.observe(viewLifecycleOwner, {
            if (it.lyricPojo != null) {
//                lyricsFragment.setLrcRows(true, it.lyricPojo!!)
            }
        })
    }

    private fun initViewPager() {
        binding.apply {

            homeFragment = newFragment()
            allGamesFragment = newFragment()

            viewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = tabsArr.size
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        TAB_1 -> homeFragment
                        TAB_2 -> allGamesFragment
                        TAB_3 -> newFragment<MineFragment>()
                        else -> throw IllegalArgumentException("createFragment error!")
                    }
                }
            }

            viewPager.isUserInputEnabled = false
            viewPager.offscreenPageLimit = 1
            viewPager.currentItem = TAB_1
        }
    }

    private fun initBottomNavigationBar() {
        binding.apply {

            bottomNavigationBar.setFab(fabHome)
            bottomNavigationBar.setMode(MODE_FIXED)
            bottomNavigationBar.setBackgroundStyle(BACKGROUND_STYLE_STATIC)

            shapeBadgeItem = ShapeBadgeItem()
                .setShapeColorResource(R.color.red)
                .setGravity(Gravity.TOP or Gravity.END)
                .setShape(SHAPE_OVAL)
                .setSizeInDp(activity, 7, 7)
                .setHideOnSelect(false)

            bottomNavigationBar
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.ic_home_white_24dp,
                        getString(R.string.main_tab_1)
                    ).setActiveColorResource(R.color.color_22d59c)
                )
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.ic_tv_white_24dp,
                        getString(R.string.main_tab_2)
                    ).setActiveColorResource(R.color.color_22d59c)
                )
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.ic_music_note_white_24dp,
                        getString(R.string.main_tab_3)
                    ).setActiveColorResource(R.color.color_22d59c).setBadgeItem(shapeBadgeItem)
                )
                .setFirstSelectedPosition(TAB_1)
                .initialise()

            bottomNavigationBar.setTabSelectedListener(object :
                OnTabSelectedListenerAdapter() {
                override fun onTabSelected(position: Int) {
                    viewPager.currentItem = position
                }
            })
        }
    }

    private fun listeners() {
        binding.apply {
            fabHome.onDebouncedClick {
                shapeBadgeItem.toggle()
                AppContext.get().appRouter.build(RouterPath.Main.MEDIA_PLAYER_PATH).navigation()
            }

            if (isOpenDebug()) {
                switchHost.visible()
                val debugModeUtils = DebugModeUtils(requireActivity())
                debugModeUtils.displayHostName(
                    DataConfig.getInstance().hostIdentification(), switchHost
                )
                switchHost.onDebouncedClick { debugModeUtils.switchHost(switchHost) }
            }
        }
    }

}