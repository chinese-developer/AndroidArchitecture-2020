package com.app.base.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 事件通知
 */
@Singleton
class EventCenter @Inject constructor() {

    /** 将听过的歌曲放入最近播放列表里，如果已经存在则忽略 */
    private val _mayAddNewMusicToPlayedList = MutableLiveData<Any>()
    private val _mayStartAnim = MutableLiveData<Int>()

    fun notifyPlayedListSetDataChanged() = _mayAddNewMusicToPlayedList.postValue(1)
    fun startAnim(whichFragment: Int) = _mayStartAnim.postValue(whichFragment)

    val mayAddNewMusicToPlayedList: LiveData<Any>
        get() = _mayAddNewMusicToPlayedList

    val mayStartAnim: LiveData<Int>
        get() = _mayStartAnim

}