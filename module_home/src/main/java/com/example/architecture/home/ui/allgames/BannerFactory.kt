package com.example.architecture.home.ui.allgames

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.example.architecture.home.R

class BannerFactory(
    val context: Context,
    private val banner: BGABanner
) {
    private val items: MutableList<String> by lazy { mutableListOf("1", "2", "3")}

    init {
        initAdapter()
    }

    private fun initAdapter() {
        if (items.isNullOrEmpty()) return
        banner.apply {
            setData(R.layout.banner_home, items, null)
            setAdapter(BGABanner.Adapter<ConstraintLayout, String> { bgaBanner: BGABanner, itemView: ConstraintLayout, _: String?, _: Int ->
               /* Glide.with(bgaBanner.context)
                    .load(R.mipmap.banner_place_holder_image)
                    .placeholder(R.mipmap.banner_place_holder_image)
                    .error(R.mipmap.banner_error_place_holder)
                    .dontAnimate()
                    .into(itemView.findViewById(R.id.iv_banner_placeholder))*/
            })
        }
    }

    companion object {

        private const val BANNER_AUTO_TURNING_TIME = 8000L

        fun build(
            context: Context,
            banner: BGABanner
        ): BannerFactory {
            return BannerFactory(context, banner)
        }
    }
}

