package com.example.architecture.home

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.utils.android.views.onDebouncedClick
import com.android.base.utils.ktx.getBlackWhiteColor
import com.app.base.app.AppBaseActivity
import com.app.base.router.RouterPath
import com.app.base.utils.SlideTrackSwitcher
import com.app.base.widget.PlayPauseDrawable
import kotlinx.android.synthetic.main.activity_media_player.*
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

@Route(path = RouterPath.Main.MEDIA_PLAYER_PATH)
class MediaPlayerActivity : AppBaseActivity() {

    private var isPlaying: Boolean = false

    private val playPauseDrawable: PlayPauseDrawable = PlayPauseDrawable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_media_player)

        updateShuffleState()
        updateRepeatState()
        updatePlayPauseDrawable()

        song_title.isSelected = true

        playPauseDrawable.setColorFilter(
            getBlackWhiteColor(ContextCompat.getColor(this, R.color.colorAccent)),
            PorterDuff.Mode.MULTIPLY
        )
        playpausefloating.setImageDrawable(playPauseDrawable)

        listeners()
    }

    private fun listeners() {
        playpausefloating.onDebouncedClick {
            isPlaying = !isPlaying
            updatePlayPauseDrawable()
        }

        object : SlideTrackSwitcher() {
            override fun onSwipeBottom() {
                finish()
            }
        }.attach(root)
    }

    private fun updateShuffleState() {
        val builder = MaterialDrawableBuilder.with(this)
            .setIcon(MaterialDrawableBuilder.IconValue.SHUFFLE)
            .setSizeDp(30)
        builder.setColor(ContextCompat.getColor(this, R.color.textSecondPrimary))
        shuffle.setImageDrawable(builder.build())
        shuffle.setOnClickListener {

        }
    }

    private fun updateRepeatState() {
        val builder = MaterialDrawableBuilder.with(this)
            .setSizeDp(30)
        builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT)
        builder.setColor(ContextCompat.getColor(this, R.color.textSecondPrimary))
        /*if (MusicPlayer.getRepeatMode() === MusicService.REPEAT_NONE) {
            builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT)
            builder.setColor(Config.textColorPrimary(getActivity(), ateKey))
        } else if (MusicPlayer.getRepeatMode() === MusicService.REPEAT_CURRENT) {
            builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE)
            builder.setColor(Config.accentColor(getActivity(), ateKey))
        } else if (MusicPlayer.getRepeatMode() === MusicService.REPEAT_ALL) {
            builder.setColor(Config.accentColor(getActivity(), ateKey))
            builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT)
        }*/
        repeat.setImageDrawable(builder.build())
        repeat.setOnClickListener {
        }
    }

    private fun updatePlayPauseDrawable() {
        if (isPlaying) {
            playPauseDrawable.transformToPause(false)
        } else {
            playPauseDrawable.transformToPlay(false)
        }
    }

}