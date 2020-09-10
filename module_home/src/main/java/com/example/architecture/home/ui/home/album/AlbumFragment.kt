package com.example.architecture.home.ui.home.album

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import com.android.base.utils.ktx.getBlackWhiteColor
import com.app.base.app.AppBaseFragment
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.drake.tooltip.toast
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentAlbumBinding
import com.example.architecture.home.repository.dataloaders.AlbumLoader
import com.example.architecture.home.ui.model.home.Album
import com.example.architecture.home.ui.model.home.Recommend
import com.example.architecture.home.utils.MediaTimberUtils
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AlbumFragment : AppBaseFragment() {

    private lateinit var binding: FragmentAlbumBinding

    private val model by viewModels<AlbumViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        binding.apply {
            list.grid(2).setup {
                addType<Album>(R.layout.item_album_preview)
                onBind {
                    val item = getModel<Album>()

                    val albumArt = findView<ImageView>(R.id.album_art)

                    displayImage(
                        item,
                        findView(R.id.footer),
                        findView(R.id.album_title),
                        findView(R.id.album_artist),
                        albumArt
                    )

//                    albumArt.transitionName = "${getString(R.string.transition_album_art)}$modelPosition"
                }

                onClick(R.id.content) {
                    toast((getModel() as Album).title + modelPosition)
                }

            }.models = AlbumLoader.getAllAlbums(activity)
        }
    }

    private fun displayImage(
        item: Album,
        footer: LinearLayout,
        albumTitle: TextView,
        albumArtist: TextView,
        albumArt: ImageView
    ) {
        val newOptions = DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).showImageOnLoading(R.mipmap.ic_empty_music)
            .displayer(FadeInBitmapDisplayer(400)).build()

        ImageLoader.getInstance()
            .displayImage(
                MediaTimberUtils.getAlbumArtUri(item.id).toString(),
                albumArt,
                newOptions,
                object : SimpleImageLoadingListener() {
                    override fun onLoadingFailed(
                        imageUri: String,
                        view: View,
                        failReason: FailReason
                    ) {
                        footer.setBackgroundColor(ContextCompat.getColor(view.context, R.color.ripple_color))
                        albumTitle.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.textPrimary
                            )
                        )
                    }

                    override fun onLoadingComplete(
                        imageUri: String,
                        view: View,
                        loadedImage: Bitmap
                    ) {
                        Palette.Builder(loadedImage).generate {
                            it?.let { palette ->
                                val swatch = palette.vibrantSwatch
                                if (swatch != null) {
                                    val color = swatch.rgb
                                    footer.setBackgroundColor(color)
                                    val textColor =
                                        getBlackWhiteColor(swatch.titleTextColor)
                                    albumTitle.setTextColor(textColor)
                                    albumArtist.setTextColor(textColor)
                                } else {
                                    val mutedSwatch = palette.mutedSwatch
                                    if (mutedSwatch != null) {
                                        val color = mutedSwatch.rgb
                                        footer.setBackgroundColor(color)
                                        val textColor =
                                            getBlackWhiteColor(mutedSwatch.titleTextColor)
                                        albumTitle.setTextColor(textColor)
                                        albumArtist.setTextColor(textColor)
                                    }
                                }
                            }
                        }
                    }
                })
    }
}