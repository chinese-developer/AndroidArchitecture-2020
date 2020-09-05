package com.app.base.data.models

import java.io.Serializable

data class Song(var url: String?) : Serializable {
    var sourceOfSongId: Long = -1L
    var title: String? = ""
    var singer: String? = ""
    var albumCoverImgId: String? = ""
    var albumCoverImgUrl: String? = "" // 歌词封面 Url，搜索的时候只能获取 id，通过 id 掉接口获取对应封面 url
    var isPlaying: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (sourceOfSongId != other.sourceOfSongId) return false

        return true
    }

    override fun hashCode(): Int {
        return sourceOfSongId.hashCode()
    }
}