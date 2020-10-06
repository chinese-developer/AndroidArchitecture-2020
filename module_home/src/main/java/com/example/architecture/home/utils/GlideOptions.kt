package com.example.architecture.home.utils

import com.bumptech.glide.request.RequestOptions
import com.example.architecture.home.R

object GlideOptions {

    val albumOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.mipmap.bg_empty_music)
        .error(R.mipmap.bg_empty_music)
}