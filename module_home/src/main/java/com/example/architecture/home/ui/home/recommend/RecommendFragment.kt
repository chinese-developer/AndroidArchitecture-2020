package com.example.architecture.home.ui.home.recommend

import android.app.SharedElementCallback
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.ktx.getBlackWhiteColor
import com.app.base.utils.scope
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentRecommendBinding
import com.example.architecture.home.ui.home.TestActivity
import com.example.architecture.home.ui.model.home.Recommend
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import kotlinx.coroutines.delay

class RecommendFragment : BaseFragment() {

    private lateinit var binding: FragmentRecommendBinding
    private val model by viewModels<RecommendViewModel>()
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        setExitSharedElementCallback(object : androidx.core.app.SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val iamgeView = binding.list.layoutManager?.findViewByPosition(index)
                    ?.findViewById<ImageView>(R.id.album_art)!!
                sharedElements.clear()
                names.clear()
                sharedElements.put(index.toString(), iamgeView)
            }
        })
    }

    private fun initAdapter() {
        binding.apply {
            list.grid(2).setup {
                addType<Recommend>(R.layout.item_recommend_preview)
                onBind {
                    val item = getModel<Recommend>()

                    val albumArt = findView<ImageView>(R.id.album_art)

                    displayImage(
                        item,
                        findView(R.id.footer),
                        findView(R.id.album_title),
                        albumArt
                    )

//                    albumArt.transitionName = "${getString(R.string.transition_album_art)}$modelPosition"
                }

                onClick(R.id.content) {
                    val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(), findView<ImageView>(
                            R.id.album_art
                        ), modelPosition.toString()
                    )
                    index = modelPosition
                    val intent = Intent(activity, TestActivity::class.java)
                    intent.putExtra("key", modelPosition)
                    startActivity(intent, options.toBundle())
                }

            }.models = model.albumDefaultItems

            swipeRefresh.onRefresh {
                scope {
                    delay(600)
                    list.models = model.albumDefaultItems
                }
            }
        }
    }

    private fun displayImage(
        item: Recommend,
        footer: ConstraintLayout,
        albumTitle: TextView,
        albumArt: ImageView
    ) {
        val newOptions = DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true).showImageOnLoading(item.coverImgResource)
            .displayer(FadeInBitmapDisplayer(400)).build()

        val imageUrl =
            if (item.coverImgUrl.isNullOrBlank()) "drawable://${item.coverImgResource}" else item.coverImgUrl

        ImageLoader.getInstance()
            .displayImage(
                imageUrl,
                albumArt,
                newOptions,
                object : SimpleImageLoadingListener() {
                    override fun onLoadingFailed(
                        imageUri: String,
                        view: View,
                        failReason: FailReason
                    ) {
                        footer.setBackgroundColor(0)
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
                                } else {
                                    val mutedSwatch = palette.mutedSwatch
                                    if (mutedSwatch != null) {
                                        val color = mutedSwatch.rgb
                                        footer.setBackgroundColor(color)
                                        val textColor =
                                            getBlackWhiteColor(mutedSwatch.titleTextColor)
                                        albumTitle.setTextColor(textColor)
                                    }
                                }
                            }
                        }
                    }
                })
    }

}