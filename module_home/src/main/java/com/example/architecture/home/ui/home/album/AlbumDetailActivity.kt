package com.example.architecture.home.ui.home.album

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import com.app.base.app.AppBaseActivity
import com.bumptech.glide.Glide
import com.example.architecture.home.R
import com.example.architecture.home.databinding.ActivityAlbumDetailBinding
import com.example.architecture.home.repository.dataloaders.AlbumLoader
import com.example.architecture.home.utils.GlideOptions.albumOptions
import com.example.architecture.home.utils.MediaTimberUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AlbumDetailActivity : AppBaseActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_detail)
        binding.lifecycleOwner = this

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                val transitionName = intent?.extras?.getString(TRANSITION_NAME) ?: ""
                sharedElements[transitionName] = binding.albumArt
            }
        })

        setupToolbar()
        setAlbumDetails()
        listeners()
    }

    private fun listeners() {
        binding.apply {

        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAlbumDetails() {
        val albumID = intent?.extras?.getLong(ALBUM_ID) ?: 0
        val album = AlbumLoader.getAlbum(this, albumID)

        val songCount: String = MediaTimberUtils.makeLabel(
            this,
            R.plurals.Nsongs,
            album.songCount
        )

        val year = if (album.year != 0) " - ${album.year}" else ""

        binding.apply {
            collapsingToolbar.title = album.title
            albumTitle.text = album.title
            albumDetails.text = "${album.artistName} - $songCount$year"
        }

        /*Glide.with(this)
            .asBitmap()
            .load(MediaTimberUtils.getAlbumArtUri(albumID).toString())
            .apply(albumOptions)
            .into(object : BitmapImageViewTarget(binding.albumArt) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.Builder(resource).generate {
                        it?.apply {
                            if (vibrantSwatch != null) {
                                setColor(vibrantSwatch!!.rgb)
                            } else {
                                setColor(mutedSwatch?.rgb)
                            }
                        }
                    }

                    super.onResourceReady(resource, transition)
                }

                private fun setColor(@ColorInt primaryColor: Int?) {
                    binding.collapsingToolbar.setContentScrimColor(primaryColor ?: 0)
                    this@AlbumDetailActivity.setStatusBarColor(primaryColor ?: 0)
                    postponeEnterTransition()
                }
            })*/

        Glide.with(this)
            .load(MediaTimberUtils.getAlbumArtUri(albumID).toString())
            .apply(albumOptions)
            .into(binding.albumArt)
    }

    companion object {
        const val ALBUM_ID = "album_id"
        const val TRANSITION_NAME = "transition_name"
    }
}