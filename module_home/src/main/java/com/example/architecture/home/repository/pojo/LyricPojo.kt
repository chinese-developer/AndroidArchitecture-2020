package com.example.architecture.home.repository.pojo

import java.io.Serializable

data class LyricPojo(
    val lyric: String,
    val tlyric: String,
    val albumCoverImageUrl: String? = ""
): Serializable