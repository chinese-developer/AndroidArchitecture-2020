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
import com.android.base.app.mvvm.VMViewModel
import com.android.base.app.mvvm.launchOnUI
import com.android.base.rx.subscribeIgnoreError
import com.android.base.utils.android.views.getString
import com.app.base.AppContext
import com.app.base.data.api.NetResult
import com.app.base.widget.dialog.TipsManager
import com.example.architecture.home.common.Constant
import com.example.architecture.home.repository.HomeApiRepository
import com.example.architecture.home.repository.pojo.AlbumCoverImageUrlPojo
import com.example.architecture.home.repository.pojo.LyricPojo
import com.app.base.data.models.Song
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.collections.set

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
        @Assisted private val savedState: SavedStateHandle,
        private val repo: HomeApiRepository
) : VMViewModel() {

    private val stableStorage by lazy { AppContext.storageManager().stableStorage() }

    private val _songModel = MutableLiveData<SongModel>()
    val songModel: LiveData<SongModel>
        get() = _songModel

    init {
        AppContext.appDataSource().observableUser().autoDispose().subscribeIgnoreError {

        }
    }

    fun logout() {
        launchOnUI {
            val response = repo.logout()

            response.whenSuccess {
                AppContext.appDataSource().logout()
                TipsManager.showMessage(getString(R.string.logout_succeed))
            }

            response.whenFailure {
                TipsManager.showMessage("${getString(R.string.error_logout_failed)} ${it.exception.message}")
            }
        }
    }

    fun fetchLyricAndAlbumCoverImgUrl(song: Song) {
        launchOnUI {
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
                    var albumCoverImageUrl: String? = song.albumCoverImgUrl

                    albumCoverResponse?.whenSuccess { imgUrlData ->
                        albumCoverImageUrl = imgUrlData.imgUrl
                    }

                    val newLyric = LyricPojo(it.lyric, it.tlyric, albumCoverImageUrl)
                    _songModel.value = createSongModel(lyricPojo = newLyric)

                    cache[sourceOfSongId] = newLyric
                    stableStorage.putEntity(Constant.LYRIC_CACHE_KEY, cache)
                }

                albumCoverResponse?.whenFailure {
                    TipsManager.showMessage(it.exception.message)
                }

                lyricResponse.whenFailure {
                    TipsManager.showMessage(it.exception.message)
                }
            }
        }
    }

    fun download(song: Song) {
        if (!repo.checkIfHasToken()) return

        if (song.url.isNullOrBlank()) {
            TipsManager.showMessage(getString(R.string.error_get_song_url))
            return
        }

        launchOnUI {
            TipsManager.showMessage(getString(R.string.tips_downloading))

            val response = repo.download(song.url!!)

            response.whenSuccess(TipsManager::showLongMessage)
            response.whenFailure { error ->
                TipsManager.showMessage(error.exception.message)
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

data class SongModel(
    var song: Song? = null,
    var lyricPojo: LyricPojo? = null,
    var pauseActionClicked: Boolean = false
)