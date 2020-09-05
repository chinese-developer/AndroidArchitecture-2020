package com.example.architecture.home

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.base.utils.android.ClipboardUtils
import com.android.base.utils.android.views.getStringArray
import com.android.base.utils.android.views.newFragment
import com.android.base.utils.android.views.onDebouncedClick
import com.android.base.utils.ktx.tryCatchAll
import com.android.cache.getEntity
import com.app.base.AppContext
import com.app.base.app.AppBaseFragment
import com.app.base.common.EventCenter
import com.app.base.data.models.Song
import com.app.base.service.DownLoadJobService
import com.app.base.service.DownLoadJobService.Companion.HANDLER_MSG_WHAT_DOWNLOAD
import com.app.base.widget.dialog.TipsManager
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.LayoutMode
import com.app.base.widget.dialog.mdstyle.core.bottomsheets.BottomSheet
import com.app.base.widget.dialog.mdstyle.core.input.input
import com.app.base.widget.dialog.mdstyle.core.list.listItems
import com.app.base.widget.dialog.mdstyle.util.lifecycleOwner
import com.app.base.widget.dialog.mdstyle.util.startActivityForUriIntent
import com.example.architecture.home.databinding.FragMainBinding
import com.example.architecture.home.common.Constant.CONTROLS_REPEAT_ALL
import com.example.architecture.home.common.Constant.CONTROLS_REPEAT_ONE
import com.example.architecture.home.common.Constant.CONTROLS_SHUFFLE
import com.example.architecture.home.common.Constant.PLAYED_MUSIC_CACHE_KEY
import com.example.architecture.home.common.Constant.PLAYING_MUSIC_CACHE_KEY
import com.example.architecture.home.common.Constant.PLAY_MODE_CACHE_KEY
import com.example.architecture.home.common.Constant.TAB_1
import com.example.architecture.home.common.Constant.TAB_2
import com.example.architecture.home.common.ZoomOutPageTransformer
import com.example.architecture.home.repository.HomeApiRepository
import com.example.architecture.home.ui.lyrics.LyricsFragment
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.reflect.TypeToken
import com.qmuiteam.qmui.kotlin.onDebounceClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_exo_control.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : AppBaseFragment() {

    private lateinit var binding: FragMainBinding

    private val tabsArr = getStringArray(R.array.home_tabs)

    private val buttonAlphaEnabled by lazy {
        resources.getInteger(R.integer.exo_media_button_opacity_percentage_enabled).toFloat() / 100
    }
    private val buttonAlphaDisabled by lazy {
        resources.getInteger(R.integer.exo_media_button_opacity_percentage_disabled).toFloat() / 100
    }

    @Inject lateinit var eventCenter: EventCenter
    @Inject lateinit var repo: HomeApiRepository

    private val handler by lazy { Handler() }
    private val storage by lazy { AppContext.storageManager().stableStorage() }
    private val jobScheduler: JobScheduler by lazy { activity?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler }

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private lateinit var defaultControlDispatcher: DefaultControlDispatcher

    private val lyricsFragment by lazy { newFragment<LyricsFragment>() }

    private val model by viewModels<MainViewModel>()

    private val updateProgressAction: Runnable = object : Runnable {
        override fun run() {
            val progress = player.currentPosition.toInt()
            lyricsFragment.seekTo(progress, false)
            handler.postDelayed(this, 900)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initExoPlayer()
        listeners()

        model.songModel.observe(viewLifecycleOwner, {
            if (it.lyricPojo != null) {
                lyricsFragment.setLrcRows(true, it.lyricPojo!!)
            }
        })
    }

    private fun initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(activity)
        binding.playerView.player = player

        defaultControlDispatcher = DefaultControlDispatcher()

        setButtonEnabled(false, binding.playerView.exo_next_custom)
        setButtonEnabled(false, binding.playerView.exo_action_more)
        setButtonEnabled(false, binding.playerView.exo_play)

        dataSourceFactory =
            DefaultDataSourceFactory(activity, Util.getUserAgent(requireActivity(), "N"))

        when (storage.getInt(PLAY_MODE_CACHE_KEY, CONTROLS_REPEAT_ONE)) {
            CONTROLS_REPEAT_ONE -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_repeat_one)
                defaultControlDispatcher.dispatchSetRepeatMode(player, REPEAT_MODE_ONE)
            }
            CONTROLS_REPEAT_ALL -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_repeat_all)
                defaultControlDispatcher.dispatchSetRepeatMode(player, REPEAT_MODE_OFF)
            }
            CONTROLS_SHUFFLE -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_shuffle)
                defaultControlDispatcher.dispatchSetRepeatMode(player, REPEAT_MODE_OFF)
            }
        }
    }

    private fun listeners() {
        binding.apply {
            player.addAnalyticsListener(object : AnalyticsListener {
                override fun onPlayerStateChanged(
                    eventTime: AnalyticsListener.EventTime?,
                    playWhenReady: Boolean,
                    playbackState: Int
                ) {
                    super.onPlayerStateChanged(eventTime, playWhenReady, playbackState)
                    when (playbackState) {
                        STATE_ENDED -> {
                        } // todo
                        STATE_IDLE, STATE_READY -> {
                            if (!player.playWhenReady) {
                                handler.removeCallbacks(updateProgressAction)
                            } else {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    if (!handler.hasCallbacks(updateProgressAction)) {
                                        handler.post(updateProgressAction)
                                    }
                                } else {
                                    handler.removeCallbacksAndMessages(0)
                                    handler.post(updateProgressAction)
                                }
                            }
                        }
                    }
                }
            })

            playerView.exo_play.onDebounceClick {
                when (player.playbackState) {
                    STATE_IDLE -> {
                    } // todo
                    STATE_ENDED -> defaultControlDispatcher.dispatchSeekTo(
                        player,
                        player.currentWindowIndex,
                        C.TIME_UNSET
                    )
                    else -> defaultControlDispatcher.dispatchSetPlayWhenReady(player, true)
                }
            }

            playerView.exo_next_custom.onDebouncedClick {

            }

            playerView.exo_prev.onDebouncedClick {

            }

            playerView.exo_repeat_toggle_custom.onDebounceClick {
                val exoRepeatToggle = playerView.exo_repeat_toggle_custom

                when (player.repeatMode) {
                    REPEAT_MODE_ONE -> {
                        playerView.exo_repeat_toggle.performClick()
                        exoRepeatToggle.setImageResource(R.mipmap.ic_controls_repeat_all)
                        storage.putInt(PLAY_MODE_CACHE_KEY, CONTROLS_REPEAT_ALL)
                    }
                    else -> {
                        if (isPlayModeShuffle()) {
                            playerView.exo_repeat_toggle.performClick()
                            exoRepeatToggle.setImageResource(R.mipmap.ic_controls_repeat_one)
                            storage.putInt(PLAY_MODE_CACHE_KEY, CONTROLS_REPEAT_ONE)
                        } else {
                            exoRepeatToggle.setImageResource(R.mipmap.ic_controls_shuffle)
                            storage.putInt(PLAY_MODE_CACHE_KEY, CONTROLS_SHUFFLE)
                        }
                    }
                }
            }
        }
    }

    private fun initViewPager() {
        binding.apply {

            viewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = tabsArr.size
                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> lyricsFragment
                        else -> newFragment<LyricsFragment>()
                    }
                }
            }

            TabLayoutMediator(tabs, viewPager, true) { tab, position ->
                val sportLabel = tabsArr[position]
                tab.text = sportLabel
            }.attach()

            viewPager.setPageTransformer(ZoomOutPageTransformer())
            viewPager.offscreenPageLimit = 2
            viewPager.currentItem = TAB_1
        }
    }

    fun playWhenReady(song: Song?, pause: Boolean = true): Boolean {
        if (song == null || song.url.isNullOrBlank()) {
            TipsManager.showMessage(getString(R.string.error_get_song_url))
            return false
        }

        with(player) {
            if (!pause) {
                mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(
                        Uri.parse(song.url)
                    )
                prepare(mediaSource)
                playWhenReady = true

                model.fetchLyricAndAlbumCoverImgUrl(song)

                cachePlayedMusic(song)

            } else {
                defaultControlDispatcher.dispatchSetPlayWhenReady(player, !playWhenReady)
            }

            setButtonEnabled(true, binding.playerView.exo_next_custom)
            setButtonEnabled(true, binding.playerView.exo_action_more)
            setButtonEnabled(true, binding.playerView.exo_play)
        }
        return true
    }

    private fun cachePlayedMusic(song: Song) {
        val playedList = playedList()
        if (!playedList.contains(song)) {
            playedList.add(song)
            storage.putEntity(PLAYED_MUSIC_CACHE_KEY, playedList)
        }

        eventCenter.notifyPlayedListSetDataChanged()
        storage.putEntity(PLAYING_MUSIC_CACHE_KEY, song)
    }

    private fun playedList(): MutableList<Song> {
        return storage.getEntity<MutableList<Song>>(
            PLAYED_MUSIC_CACHE_KEY,
            object : TypeToken<MutableList<Song>>() {}.type
        ) ?: mutableListOf()
    }

    fun isPlayModeShuffle(): Boolean {
        return binding.playerView.exo_repeat_toggle_custom.drawable.constantState?.equals(
            ContextCompat.getDrawable(
                requireActivity(),
                R.mipmap.ic_controls_shuffle
            )?.constantState
        ) == true
    }

    fun onLyricsSeekToListener(progress: Int) {
        player.seekTo(progress.toLong())
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
                                        TipsManager.showMessage(errorMessage)
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

    private fun setButtonEnabled(enabled: Boolean, view: View?) {
        if (view == null) {
            return
        }
        view.isEnabled = enabled
        view.alpha = if (enabled) buttonAlphaEnabled else buttonAlphaDisabled
    }

    /** 仅当单曲循环模式时，不会触发 [player.addAnalyticsListener] 的 onPlayerStateChanged 回调，其他模式通过回调开启 runnable */
    private fun postUpdateProgressActionWhenRepeatModeOne() {
        if (::player.isInitialized &&
            player.playWhenReady &&
            player.repeatMode == REPEAT_MODE_ONE
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                if (!handler.hasCallbacks(updateProgressAction)) {
                    handler.post(updateProgressAction)
                }
            } else {
                handler.removeCallbacksAndMessages(0)
                handler.post(updateProgressAction)
            }
            handler.post(updateProgressAction)
        }
    }

    private fun releasePlayer() {
        Assertions.checkNotNull(binding.playerView).player = null
        player.release()
    }

    private fun playingMusic(): Song? {
        return storage.getEntity<Song>(PLAYING_MUSIC_CACHE_KEY)
    }

    override fun onResume() {
        super.onResume()

        postUpdateProgressActionWhenRepeatModeOne()
    }

    override fun onStop() {
        super.onStop()
        // 息屏时关闭歌词滚动
        handler.removeCallbacks(updateProgressAction)
    }

    override fun onDestroyView() {
        binding.playerView.onPause()
        handler.removeCallbacks(updateProgressAction)
        releasePlayer()
        super.onDestroyView()
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