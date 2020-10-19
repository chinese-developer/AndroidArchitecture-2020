package com.example.architecture.home.repository.pojo

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class AlbumCoverImageUrlPojo(
    @SerializedName("url")
    val imgUrl: String?
): Serializable