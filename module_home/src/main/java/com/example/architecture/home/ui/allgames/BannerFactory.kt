package com.example.architecture.home.ui.allgames

import android.content.Context
import android.graphics.Color
import androidx.viewpager2.widget.ViewPager2
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.banner.Banner
import com.android.base.widget.banner.IndicatorView
import com.example.architecture.home.R

class BannerFactory(
    val context: Context,
    val banner: Banner
) {
    private val adapter = BindingAdapter()

    private val indicator by lazy {
        IndicatorView(context)
                .setIndicatorRatio(4f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRatio(6f)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorSpacing(0f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_CIRCLE_RECT)
                .setIndicatorColor(Color.parseColor("#1AFFFFFF"))
                .setIndicatorSelectorColor(Color.parseColor("#99FFFFFF"))
    }

    private val items: MutableList<String> by lazy { mutableListOf("1", "2", "3")}

    init {

        initAdapter()
    }

    private fun initAdapter() {
        if (items.isNullOrEmpty()) return
        adapter.setContext(context)
        adapter.addType<String>(R.layout.item_banner)
        adapter.addModels(items)
        banner.setAutoPlay(true)
                .setOffscreenPageLimit(1)
                .setIndicator(indicator)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL).setPagerScrollDuration(800)
                .setAutoTurningTime(BANNER_AUTO_TURNING_TIME)
                .setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {


                    }
                }).adapter = adapter
    }

    companion object {

        private const val BANNER_AUTO_TURNING_TIME = 8000L

        fun build(
            context: Context,
            banner: Banner
        ): BannerFactory {
            return BannerFactory(context, banner)
        }
    }
}

