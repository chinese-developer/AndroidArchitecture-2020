/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.common

import com.app.base.data.models.Song

internal typealias OnActionMoreClick = (song: Song?, pos: Int) -> Unit

enum class PlayAction {
    PLAY_ACTION_NEXT, PLAY_ACTION_PREVIOUS, PLAY_ACTION_SHUFFLE
}

object Constant {

    const val Invalid = -1

    /** 分页一次请求数量为50个条目 */
    const val PAGE_SIZE = 50

    /** fragment tab */
    const val TAB_1: Int = 0
    const val TAB_2: Int = 1

    /** play mode */
    const val CONTROLS_REPEAT_ONE: Int = 0
    const val CONTROLS_REPEAT_ALL: Int = 1
    const val CONTROLS_SHUFFLE: Int = 2

    /** 当前正在播放的列表是：[最近播放] [歌单广场] */
    const val PLAYING_LIST_CACHE_KEY = "playing_list_cache_key"

    /** 当前正在播放的歌曲 [com.example.architecture.home.main.model.Song]*/
    const val PLAYING_MUSIC_CACHE_KEY = "playing_music_cache_key"

    /** 已经播放过的歌曲列表 */
    const val PLAYED_MUSIC_CACHE_KEY = "played_music_cache_key"

    /** 播放模式，单曲循环[CONTROLS_REPEAT_ONE]，列表循环[CONTROLS_REPEAT_ALL]，随机播放[CONTROLS_SHUFFLE] */
    const val PLAY_MODE_CACHE_KEY = "play_mode_cache_key"

    /** 已经下载过的歌词 */
    const val LYRIC_CACHE_KEY = "lyric_cache_key"

}