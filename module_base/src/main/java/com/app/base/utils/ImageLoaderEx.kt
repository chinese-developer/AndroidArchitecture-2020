package com.app.base.utils

import android.widget.ImageView
import com.android.base.imageloader.DisplayConfig
import com.android.base.imageloader.ImageLoader
import com.app.base.R

private val appDisplayConfig = DisplayConfig
        .create()
        .setErrorPlaceholder(R.drawable.shape_app_icon_default)
        .setLoadingPlaceholder(R.drawable.shape_app_icon_default)

fun ImageLoader.displayAppIcon(fragment: androidx.fragment.app.Fragment, imageView: ImageView, url: String?) {
    display(fragment, imageView, url, appDisplayConfig)
}