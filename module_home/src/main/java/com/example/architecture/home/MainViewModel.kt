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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    suspend fun getUser() = suspendCoroutine<String> { continuation ->
        getUser {
            continuation.resume(it)
        }
    }

    suspend fun getUserCoroutine() = suspendCoroutine<String> { continuation ->
        getUser(object : CallbackWithError<String> {
            override fun onSuccess(value: String) {
                continuation.resume(value)
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

    }

    fun logout() {
        viewModelScope.launch {
            val response = repo.logout()

            response.whenSuccess {
                AppContext.appDataSource().logout()
                toast(getString(R.string.logout_succeed))
            }

            response.whenFailure {
                toast("${getString(R.string.error_logout_failed)} ${it.exception.message}")
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