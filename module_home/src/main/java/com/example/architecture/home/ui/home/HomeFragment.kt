package com.example.architecture.home.ui.home

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.app.fragment.BaseFragment
import com.android.base.interfaces.adapter.OnTabSelectedListenerAdapter
import com.android.base.utils.android.ClipboardUtils
import com.android.base.utils.android.views.getStringArray
import com.android.base.utils.android.views.newFragment
import com.android.base.utils.ktx.tryCatchAll
import com.app.base.data.models.Song
import com.app.base.service.DownLoadJobService
import com.app.base.service.DownLoadJobService.Companion.HANDLER_MSG_WHAT_DOWNLOAD
import com.app.base.toast
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.LayoutMode
import com.app.base.widget.dialog.mdstyle.core.bottomsheets.BottomSheet
import com.app.base.widget.dialog.mdstyle.core.input.input
import com.app.base.widget.dialog.mdstyle.core.list.listItems
import com.app.base.widget.dialog.mdstyle.util.lifecycleOwner
import com.app.base.widget.dialog.mdstyle.util.startActivityForUriIntent
import com.example.architecture.home.R
import com.example.architecture.home.common.Constant.TAB_1
import com.example.architecture.home.common.Constant.TAB_2
import com.example.architecture.home.common.Constant.TAB_3
import com.example.architecture.home.common.Constant.TAB_4
import com.example.architecture.home.databinding.FragHomeBinding
import com.example.architecture.home.repository.HomeApiRepository
import com.example.architecture.home.ui.home.album.AlbumFragment
import com.example.architecture.home.ui.home.lyrics.LyricsFragment
import com.example.architecture.home.ui.home.square.SquareWithRecyclerViewFragment
import com.example.architecture.home.ui.home.recommend.RecommendFragment
import com.example.architecture.home.ui.home.square.SquareWithViewPagerFragment
import com.example.architecture.home.ui.home.square.setOverScrollModeNever
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragHomeBinding

    private val tabsArr = getStringArray(R.array.home_tabs)

    @Inject lateinit var repo: HomeApiRepository

    private val jobScheduler: JobScheduler by lazy { activity?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler }

    private val recommendFragment by lazy { newFragment<RecommendFragment>() }
    private val albumFragment by lazy { newFragment<AlbumFragment>() }
    private val lyricsFragment by lazy { newFragment<LyricsFragment>() }
    private val squareWithRvFragment by lazy { newFragment<SquareWithRecyclerViewFragment>() }
    private val squareWithVpFragment by lazy { newFragment<SquareWithViewPagerFragment>() }

    private val model by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        binding.apply {

            viewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = tabsArr.size
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        TAB_1 -> recommendFragment
                        TAB_2 -> albumFragment
                        TAB_3 -> squareWithRvFragment
                        TAB_4 -> squareWithVpFragment
                        else -> throw IllegalArgumentException("createFragment error!")
                    }
                }
            }

            TabLayoutMediator(tabs, viewPager, true) { tab, position ->
                tab.customView = getTabView(position)
            }.attach()

            updateTabStatusWhenSelected(tabs.getTabAt(0)!!, true, tabsArr[0])

            tabs.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    updateTabStatusWhenSelected(tab, true, tabsArr[tab.position])
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    updateTabStatusWhenSelected(tab, false, tabsArr[tab.position])
                }
            })

            viewPager.setOverScrollModeNever()
            viewPager.offscreenPageLimit = tabsArr.size
            viewPager.currentItem = TAB_1
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun downloadSong(song: Song) {
        MaterialDialog(requireActivity(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            listItems(res = R.array.action_more, waitForPositiveButton = false) { _, indices, _ ->
                dismiss()
                when (indices) {
                    0 -> model.download(song)
                    1 -> {
                        MaterialDialog(requireActivity()).show {
                            title(res = R.string.tips_shared_url_title)
                            message(getString(R.string.tips_shared_content_, song.title)) {
                                lineSpacing(1.4f)
                                html {
                                    context.startActivityForUriIntent(it) { errorMessage ->
                                        toast(errorMessage)
                                    }
                                }
                            }
                            input(
                                preFill = song.url,
                                inputType = InputType.TYPE_CLASS_NUMBER
                            )
                            positiveButton(getString(R.string.actionbar_webview_cope)) {
                                ClipboardUtils.copyText(song.url)
                            }
                            lifecycleOwner(viewLifecycleOwner)
                        }
                    }
                }
            }
            lifecycleOwner(viewLifecycleOwner)
        }
    }

    @SuppressLint("InflateParams")
    private fun getTabView(position: Int): View {
        return LayoutInflater.from(activity).inflate(R.layout.item_tab, null, false).apply {
            val tabTitle = findViewById<TextView>(R.id.tab_title)
            tabTitle.text = tabsArr[position]
        }
    }

    private fun updateTabStatusWhenSelected(tab: TabLayout.Tab, isSelect: Boolean, label: String) {
        tab.customView?.findViewById<TextView>(R.id.tab_title)?.apply {
            typeface = if (isSelect) {
                textSize = 20F
                Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                textSize = 18F
                Typeface.defaultFromStyle(Typeface.NORMAL)
            }
            text = label
        }
    }

    fun showPlayingFragment() {
        binding.viewPager.currentItem = TAB_2
    }

    fun downloadSongUrlWhenAppBackground(bundle: PersistableBundle) {
        tryCatchAll {
            val builder = JobInfo.Builder(
                HANDLER_MSG_WHAT_DOWNLOAD,
                ComponentName(requireActivity(), DownLoadJobService::class.java)
            )
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // 需要请求网络
            jobScheduler.schedule(builder.build())
        }
    }
}