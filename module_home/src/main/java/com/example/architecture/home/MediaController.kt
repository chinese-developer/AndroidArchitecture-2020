package com.example.architecture.home

import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.android.base.app.activity.ActivityDelegate
import com.android.base.utils.ktx.onDebouncedClick
import com.android.cache.getEntity
import com.app.base.AppContext
import com.app.base.common.EventCenter
import com.app.base.data.models.Song
import com.app.base.toast
import com.example.architecture.home.common.Constant
import com.example.architecture.home.databinding.FragMainBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.layout_exo_control.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MediaController(
    private val host: MainFragment,
    private val binding: FragMainBinding,
    private val model: MainViewModel,
    private val eventCenter: EventCenter
) : ActivityDelegate<MainActivity> {

    private val storage by lazy { AppContext.storageManager().stableStorage() }

    private var activity: MainActivity = host.activity as MainActivity
    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private lateinit var defaultControlDispatcher: DefaultControlDispatcher

    private val buttonAlphaEnabled by lazy {
        host.resources.getInteger(R.integer.exo_media_button_opacity_percentage_enabled)
            .toFloat() / 100
    }
    private val buttonAlphaDisabled by lazy {
        host.resources.getInteger(R.integer.exo_media_button_opacity_percentage_disabled)
            .toFloat() / 100
    }

    private val handler by lazy { Handler() }
    private val updateProgressAction: Runnable = object : Runnable {
        override fun run() {
            val progress = player.currentPosition.toInt()
//            lyricsFragment.seekTo(progress, false)
            handler.postDelayed(this, 900)
        }
    }

    init {
        activity.addDelegate(this)
        initExoPlayer()
        listeners()
    }

    private fun initExoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(host.activity)
        binding.playerView.player = player

        defaultControlDispatcher = DefaultControlDispatcher()

        setButtonEnabled(false, binding.playerView.exo_next_custom)
        setButtonEnabled(false, binding.playerView.exo_action_more)
        setButtonEnabled(false, binding.playerView.exo_play)

        dataSourceFactory =
            DefaultDataSourceFactory(activity, Util.getUserAgent(activity, "N"))

        when (storage.getInt(Constant.PLAY_MODE_CACHE_KEY, Constant.CONTROLS_REPEAT_ONE)) {
            Constant.CONTROLS_REPEAT_ONE -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_repeat_one)
                defaultControlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_ONE)
            }
            Constant.CONTROLS_REPEAT_ALL -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_repeat_all)
                defaultControlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_OFF)
            }
            Constant.CONTROLS_SHUFFLE -> {
                binding.playerView.exo_repeat_toggle_custom.setImageResource(R.mipmap.ic_controls_shuffle)
                defaultControlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_OFF)
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
                        Player.STATE_ENDED -> {
                        } // todo
                        Player.STATE_IDLE, Player.STATE_READY -> {
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

            playerView.exo_play.onDebouncedClick {
                when (player.playbackState) {
                    Player.STATE_IDLE -> {
                    } // todo
                    Player.STATE_ENDED -> defaultControlDispatcher.dispatchSeekTo(
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

            playerView.exo_repeat_toggle_custom.onDebouncedClick {
                val exoRepeatToggle = playerView.exo_repeat_toggle_custom

                when (player.repeatMode) {
                    Player.REPEAT_MODE_ONE -> {
                        playerView.exo_repeat_toggle.performClick()
                        exoRepeatToggle.setImageResource(R.mipmap.ic_controls_repeat_all)
                        storage.putInt(Constant.PLAY_MODE_CACHE_KEY, Constant.CONTROLS_REPEAT_ALL)
                    }
                    else -> {
                        if (isPlayModeShuffle()) {
                            playerView.exo_repeat_toggle.performClick()
                            exoRepeatToggle.setImageResource(R.mipmap.ic_controls_repeat_one)
                            storage.putInt(
                                Constant.PLAY_MODE_CACHE_KEY,
                                Constant.CONTROLS_REPEAT_ONE
                            )
                        } else {
                            exoRepeatToggle.setImageResource(R.mipmap.ic_controls_shuffle)
                            storage.putInt(Constant.PLAY_MODE_CACHE_KEY, Constant.CONTROLS_SHUFFLE)
                        }
                    }
                }
            }
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
            player.repeatMode == Player.REPEAT_MODE_ONE
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
        return storage.getEntity<Song>(Constant.PLAYING_MUSIC_CACHE_KEY)
    }

    private fun cachePlayedMusic(song: Song) {
        val playedList = playedList()
        if (!playedList.contains(song)) {
            playedList.add(song)
            storage.putEntity(Constant.PLAYED_MUSIC_CACHE_KEY, playedList)
        }

        eventCenter.notifyPlayedListSetDataChanged()
        storage.putEntity(Constant.PLAYING_MUSIC_CACHE_KEY, song)
    }

    private fun playedList(): MutableList<Song> {
        return storage.getEntity<MutableList<Song>>(
            Constant.PLAYED_MUSIC_CACHE_KEY,
            object : TypeToken<MutableList<Song>>() {}.type
        ) ?: mutableListOf()
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

    override fun onDestroy() {
        binding.playerView.onPause()
        handler.removeCallbacks(updateProgressAction)
        releasePlayer()
        super.onDestroy()
    }

    fun playWhenReady(song: Song?, pause: Boolean = true): Boolean {
        if (song == null || song.url.isNullOrBlank()) {
            toast(activity.getString(R.string.error_get_song_url))
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

    fun isPlayModeShuffle(): Boolean {
        return binding.playerView.exo_repeat_toggle_custom.drawable.constantState?.equals(
            ContextCompat.getDrawable(
                activity,
                R.mipmap.ic_controls_shuffle
            )?.constantState
        ) == true
    }

    fun onLyricsSeekToListener(progress: Int) {
        player.seekTo(progress.toLong())
    }

}