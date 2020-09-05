package com.example.architecture.home.repository.pojo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AlbumListPojo(
    val code: Int,
    @SerializedName("playlist")
    val data: PlayList
) : Serializable {

    data class PlayList(
        val tracks: List<Tracks>,
        val coverImgUrl: String
    ) {

        data class Tracks(
            val name: String,
            val id: Long,
            @SerializedName("ar")
            val artist: List<Artist>?,
            @SerializedName("al")
            val song: SongInfo?
        ) {
            data class Artist(
                val id: Long,
                val name: String
            )

            data class SongInfo(
                    @SerializedName("picUrl")
                    val picUrl: String,
                    @SerializedName("pic_str")
                    val picId: String
            )

        }
    }
}