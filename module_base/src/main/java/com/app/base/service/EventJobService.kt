package com.app.base.service

import com.app.base.data.models.Song

class EventJobService {

    /** 当 app 进入后台后，启动 jobService 下载歌曲 url 并将 url 地址通知回监听者 */
    private var song: Song? = null
    private var whichFragment: String? = null

    fun setSong(song: Song, whichFragment: String?) {
        this.song = song
        this.whichFragment = whichFragment
    }

    fun getSong(): Song? {
        return song
    }

    fun songListFocusOn(): String? {
        return whichFragment
    }

    companion object {
        const val PLAY_LIST_FRAGMENT = "play_list_fragment"
        const val ALBUM_LIST_FRAGMENT = "album_list_fragment"
    }

}