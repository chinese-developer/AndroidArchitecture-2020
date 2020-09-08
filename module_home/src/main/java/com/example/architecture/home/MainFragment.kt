/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.views.getStringArray
import com.android.base.utils.android.views.newFragment
import com.android.base.utils.android.views.onDebouncedClick
import com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_STATIC
import com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem.SHAPE_OVAL
import com.drake.tooltip.toast
import com.example.architecture.home.common.Constant.TAB_1
import com.example.architecture.home.common.Constant.TAB_2
import com.example.architecture.home.common.OnTabSelectedListenerAdapter
import com.example.architecture.home.databinding.FragMainBinding
import com.example.architecture.home.ui.home.HomeFragment
import com.example.architecture.home.ui.home.playlist.PlayListFragment
import com.example.architecture.home.ui.home.recommend.RecommendFragment
import com.example.architecture.home.ui.mine.MineFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private lateinit var binding: FragMainBinding
    private lateinit var shapeBadgeItem: ShapeBadgeItem
    private lateinit var homeFragment: HomeFragment
    private lateinit var mineFragment: MineFragment

    private val tabsArr = getStringArray(R.array.main_tabs)

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

        initViewPager()
        initBottomNavigationBar()
        listeners()
    }

    private fun initViewPager() {
        binding.apply {

            homeFragment = newFragment()
            mineFragment = newFragment()

            viewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = tabsArr.size
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        TAB_1 -> homeFragment
                        TAB_2 -> mineFragment
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
                .setHideOnSelect(true)

            bottomNavigationBar
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.ic_home_white_24dp,
                        getString(R.string.main_tab_1)
                    ).setActiveColorResource(R.color.color_1DE9B6)
                )
                .addItem(
                    BottomNavigationItem(
                        R.mipmap.ic_music_note_white_24dp,
                        getString(R.string.main_tab_2)
                    ).setActiveColorResource(R.color.color_1DE9B6).setBadgeItem(shapeBadgeItem)
                )
                .setFirstSelectedPosition(TAB_1)
                .initialise()

            binding.bottomNavigationBar.setTabSelectedListener(object :
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
                toast("点我想干嘛？")
            }
        }
    }

}