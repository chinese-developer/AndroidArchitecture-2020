package com.example.architecture.home.repository.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AlbumCoverImageUrlPojo(
    @SerializedName("url")
    val imgUrl: String?
): Serializable