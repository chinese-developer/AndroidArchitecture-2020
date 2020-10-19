package com.example.architecture.home.repository.pojo

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class SearchListPojo(
    val id: Long,
    val name: String,
    val artist: List<String>?,
    val album: String,
    @SerializedName("pic_id")
    val picId: String,
    @SerializedName("url_id")
    val UrlId: Long,
    @SerializedName("lyric_id")
    val lyricId: Long,
    val source: String // 哪个平台： netease 网易云
): Serializable