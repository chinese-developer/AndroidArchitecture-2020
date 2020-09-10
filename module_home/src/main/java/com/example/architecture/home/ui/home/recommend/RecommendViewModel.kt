package com.example.architecture.home.ui.home.recommend

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.architecture.home.R
import com.example.architecture.home.ui.model.home.Recommend

class RecommendViewModel @ViewModelInject constructor(
) : ViewModel() {

    internal val albumDefaultItems = mutableListOf(
        Recommend(-1L, "正在播放", coverImgResource = R.mipmap.bg_album_playing),
        Recommend(3778678L, "云音乐热歌榜", coverImgResource = R.mipmap.bg_album_rgb),
        Recommend(64016L, "中国TOP排行榜（内地榜）", coverImgResource = R.mipmap.bg_album_china_top_100),
        Recommend(112504L, "中国TOP排行榜（港台榜）", coverImgResource = R.mipmap.bg_album_china_top_100_hk),
        Recommend(19723756L, "云音乐飙升榜", coverImgResource = R.mipmap.bg_album_bsb),
        Recommend(3779629L, "云音乐新歌榜", coverImgResource = R.mipmap.bg_album_xgb),
        Recommend(2884035L, "网易原创歌曲榜", coverImgResource = R.mipmap.bg_album_ycb)
    )
}