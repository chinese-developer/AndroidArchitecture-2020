package com.example.architecture.home.ui.home.lyrics

import androidx.annotation.RestrictTo
import com.android.base.utils.ktx.tryCatchAll
import com.example.architecture.home.ui.home.lyrics.parser.DefaultLrcParser
import com.example.architecture.home.ui.home.lyrics.parser.LrcRow
import com.example.architecture.home.repository.pojo.LyricPojo

/** 获取歌词 List */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun getLrcRows(lyricPojo: LyricPojo): List<LrcRow?>? {
    var rows: List<LrcRow>? = null
    tryCatchAll {
        rows = DefaultLrcParser.getIstance().getLrcRows(lyricPojo.lyric)
    }
    return rows
}