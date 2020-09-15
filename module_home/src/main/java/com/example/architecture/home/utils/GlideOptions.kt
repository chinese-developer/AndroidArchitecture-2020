package com.example.architecture.home.utils

import com.bumptech.glide.request.RequestOptions
import com.example.architecture.home.R

object GlideOptions {

    val albumOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.mipmap.ic_empty_music)
        .error(R.mipmap.ic_empty_music)
}