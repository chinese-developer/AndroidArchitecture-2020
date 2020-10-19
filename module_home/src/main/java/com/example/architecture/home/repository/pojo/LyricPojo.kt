package com.example.architecture.home.repository.pojo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class LyricPojo(
    val lyric: String,
    val tlyric: String,
    val albumCoverImageUrl: String? = ""
): Serializable