/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.android.base.app.mvvm.VMViewModel
import com.android.base.app.mvvm.launchOnUI
import com.android.base.rx.subscribeIgnoreError
import com.android.base.utils.ktx.getString
import com.app.base.AppContext
import com.app.base.dagger.CoroutinesDispatcherProvider
import com.app.base.data.api.NetResult
import com.app.base.data.models.Song
import com.app.base.toast
import com.drake.tooltip.toast
import com.example.architecture.home.common.Constant
import com.example.architecture.home.repository.HomeApiRepository
import com.example.architecture.home.repository.pojo.AlbumCoverImageUrlPojo
import com.example.architecture.home.repository.pojo.LyricPojo
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.concurrent.thread
import kotlin.coroutines.*

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    @Assisted private val savedState: SavedStateHandle,
    private val repo: HomeApiRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : VMViewModel() {

    private val stableStorage by lazy { AppContext.storageManager().stableStorage() }

    private val _songModel = MutableLiveData<SongModel>()
    val songModel: LiveData<SongModel>
        get() = _songModel

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    }

    init {
        AppContext.appDataSource()
            .observableUser()
            .autoDispose()
            .subscribeIgnoreError {

            }
    }

    fun main() {

    }

    suspend fun getUser() = suspendCoroutine<String> { continuation ->
        getUser {
            continuation.resume(it)
        }
    }

    suspend fun getUserCoroutine() = suspendCoroutine<String> { continuation ->
        // continuation 是 SafeContinuation 的, 这个类是协程本体 SuspendLambda 的包装类
        // 它的作用是保证 SuspendLambda 的 resume() 只能被执行一次, 同时在没有切线程不要调用它而直接返回.
        // 执行流程是:
        // 1. 首先调用包装类 SafeContinuation 的 resume(), 包装类的 resume 会调用内部的拦截器 Intercepted 的 resume() 进行线程切换
        // 2. 拦截器 每次执行时或第一次执行时, 都会去拦截本体 SuspendLambda
        // 3. 完成上述后最终会调用协程本体 SuspendLambda 的 resume() 通知本体恢复执行.
        getUser(object : CallbackWithError<String> {
            override fun onSuccess(value: String) {
                // 在当前线程调用栈上直接调用, 则不会挂起
                continuation.resume(value)
                // 真正的挂起必须保证切线程后才会挂起
                thread {
                    continuation.resume(value)
                }
            }

            override fun onError(t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }

    fun getUser(callback: Callback) {
        callback.invoke("asd")
    }

    fun getUser(callback: CallbackWithError<String>) {
        suspend {
            // suspend 大括号内是协程函数体的本体(类名:SuspendLambda)
        }.createCoroutine(object : Continuation<Unit> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Unit>) {
                TODO("Not yet implemented")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            val response = repo.logout()

            response.whenSuccess {
                AppContext.appDataSource().logout()
                toast(getString(R.string.msg_logout_succeed))
            }

            response.whenFailure {
                toast("${getString(R.string.msg_logout_failed)} ${it.exception.message}")
            }
        }
    }

    fun fetchLyricAndAlbumCoverImgUrl(song: Song) {
        viewModelScope.launch {
            val sourceOfSongId = song.sourceOfSongId
            val cache = stableStorage.getEntity<HashMap<Long, LyricPojo>>(
                Constant.LYRIC_CACHE_KEY,
                object : TypeToken<HashMap<Long, LyricPojo>>() {}.type
            ) ?: hashMapOf()

            if (cache.size > 0 && cache[sourceOfSongId] != null) {
                _songModel.value = createSongModel(lyricPojo = cache[sourceOfSongId])
            } else {
                val lyricResponse = repo.getLyric(sourceOfSongId)

                val albumCoverResponse = maybeFetchAlbumCoverImgUrl(song)

                lyricResponse.whenSuccess {
                    if (it != null) {
                        var albumCoverImageUrl: String? = song.albumCoverImgUrl

                        albumCoverResponse?.whenSuccess { imgUrlData ->
                            albumCoverImageUrl = imgUrlData?.imgUrl
                        }

                        val newLyric = LyricPojo(it.lyric, it.tlyric, albumCoverImageUrl)
                        _songModel.value = createSongModel(lyricPojo = newLyric)

                        cache[sourceOfSongId] = newLyric
                        stableStorage.putEntity(Constant.LYRIC_CACHE_KEY, cache)
                    }
                }

                albumCoverResponse?.whenFailure {
                    toast(it.exception.message ?: "")
                }

                lyricResponse.whenFailure {
                    toast(it.exception.message ?: "")
                }
            }
        }
    }

    fun download(song: Song) {

        if (!repo.checkIfHasToken()) return

        if (song.url.isNullOrBlank()) {
            toast(getString(R.string.error_get_song_url))
            return
        }

        launchOnUI {
            toast(getString(R.string.tips_downloading))

            val response = repo.download(song.url!!)

            response.whenSuccess {
                if (it != null) AppContext.get().toast(it)
            }
            response.whenFailure { error ->
                toast(error.exception.message)
            }
        }
    }

    private suspend fun maybeFetchAlbumCoverImgUrl(song: Song): NetResult<AlbumCoverImageUrlPojo>? {
        if (song.albumCoverImgUrl.isNullOrBlank()) {
            return repo.getAlbumCoverImgUrlById(song.albumCoverImgId)
        }
        return null
    }

    private fun createSongModel(
        song: Song? = null,
        lyricPojo: LyricPojo? = null
    ): SongModel {
        return SongModel(
            song = song,
            lyricPojo = lyricPojo
        )
    }
}

typealias Callback = (String) -> Unit

interface CallbackWithError<T> {
    fun onSuccess(value: T)
    fun onError(t: Throwable)
}

data class SongModel(
    var song: Song? = null,
    var lyricPojo: LyricPojo? = null,
    var pauseActionClicked: Boolean = false
)