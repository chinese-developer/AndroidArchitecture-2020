package com.example.architecture.home.ui.home.album

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.compat.immersiveDark
import com.android.base.utils.ktx.getBlackWhiteColor
import com.android.base.widget.adapter.utils.grid
import com.android.base.widget.adapter.utils.setup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentAlbumBinding
import com.example.architecture.home.repository.dataloaders.AlbumLoader
import com.example.architecture.home.ui.home.album.AlbumDetailActivity.Companion.ALBUM_ID
import com.example.architecture.home.ui.home.album.AlbumDetailActivity.Companion.TRANSITION_NAME
import com.example.architecture.home.ui.model.home.Album
import com.example.architecture.home.utils.MediaTimberUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class AlbumFragment : BaseFragment() {

    private lateinit var binding: FragmentAlbumBinding
    private var sharedElementPosition = -1

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

    override fun onResume() {
        super.onResume()
        activity?.immersiveDark()
    }

    private fun initAdapter() {
        binding.apply {

            list.grid(2).setup {
                addType<Album>(R.layout.item_album_preview)
                onBind {
                    val item = getModel<Album>()

                    val albumArt = findView<ImageView>(R.id.album_art)
                    val transitionView = findView<ConstraintLayout>(R.id.content)

                    displayImage(
                        item,
                        findView(R.id.footer),
                        findView(R.id.album_title),
                        findView(R.id.album_artist),
                        albumArt
                    )

                    // 为每个不同的 item 设置 transitionName
                    transitionView.transitionName =
                        "${getString(R.string.transition_album_art)}$modelPosition"
                }

                onClick(R.id.content) {
                    sharedElementPosition = modelPosition
                    val model = getModel<Album>()
                    val transitionView = findView<ConstraintLayout>(R.id.content)
                    val sharedElementName = ViewCompat.getTransitionName(transitionView)!!

                    val options: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            requireActivity(),
                            transitionView,
                            sharedElementName
                        )
                    val intent = Intent(activity, AlbumDetailActivity::class.java)
                    intent.putExtra(ALBUM_ID, model.id)
                    intent.putExtra(TRANSITION_NAME, sharedElementName)
                    startActivity(intent, options.toBundle())
                }

            }.models = AlbumLoader.getAllAlbums(activity)

            setExitSharedElementCallback()
        }
    }

    private fun setExitSharedElementCallback() {
        object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val holder = binding.list.findViewHolderForAdapterPosition(sharedElementPosition)
                if (holder?.itemView == null) return

                sharedElements[names[0]] = holder.itemView.findViewById(R.id.album_art)
            }
        }
    }

    private fun setEnterSharedElementCallback(target: Fragment) {
        object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val view = target.view ?: return
                sharedElements[names[0]] = view.findViewById(R.id.artist_art)
            }
        }
    }

    private fun displayImage(
        item: Album,
        footer: LinearLayout,
        albumTitle: TextView,
        albumArtist: TextView,
        albumArt: ImageView
    ) {
        val options = RequestOptions().apply {
            placeholder(R.mipmap.bg_empty_music)
            error(R.mipmap.bg_empty_music)
//            transform(RoundedCorners(resources.getDimensionPixelOffset(R.dimen.common_corner_radius_8)))
        }
        Glide.with(this)
            .setDefaultRequestOptions(options)
            .asBitmap()
            .load(MediaTimberUtils.getAlbumArtUri(item.id).toString())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .transition(DrawableTransitionOptions.withCrossFade())
            .addListener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    setColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.ripple_color
                        ), ContextCompat.getColor(
                            context!!,
                            R.color.textPrimary
                        )
                    )
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { loadBitmapResource ->
                        Palette.Builder(loadBitmapResource).generate {
                            it?.vibrantSwatch?.let { swatch ->
                                setColor(
                                    swatch.rgb,
                                    getBlackWhiteColor(swatch.titleTextColor)
                                )
                            } ?: it?.mutedSwatch?.let { mutedSwatch ->
                                setColor(
                                    mutedSwatch.rgb,
                                    getBlackWhiteColor(mutedSwatch.titleTextColor)
                                )
                            }
                        }
                    }
                    return false
                }

                private fun setColor(@ColorInt bgColor: Int?, @ColorInt textColor: Int?) {
                    footer.setBackgroundColor(bgColor ?: 0)
                    albumTitle.setTextColor(textColor ?: 0)
                    albumArtist.setTextColor(textColor ?: 0)
                }
            })
            .into(albumArt)
    }
}