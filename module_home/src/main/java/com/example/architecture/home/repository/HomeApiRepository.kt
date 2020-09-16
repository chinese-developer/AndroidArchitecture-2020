/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.repository

import com.android.sdk.net.error.ErrorException
import com.app.base.AppContext
import com.app.base.app.BaseRepository
import com.app.base.dagger.OKHTTP_REGULAR
import com.app.base.dagger.OKHTTP_WITHOUT_TOKEN
import com.app.base.data.api.NetResult
import com.app.base.data.app.AppDataSource
import com.app.base.data.models.NetEase
import com.app.base.data.models.Song
import com.app.base.utils.IOUtils.copyInputStreamToFile
import com.app.base.utils.IOUtils.createFileToDownLoadDir
import com.example.architecture.home.HomeApiService
import com.example.architecture.home.repository.pojo.AlbumCoverImageUrlPojo
import com.example.architecture.home.repository.pojo.AlbumListPojo
import com.example.architecture.home.repository.pojo.LyricPojo
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class HomeApiRepository @Inject constructor(
    private val appDataSource: AppDataSource,
    @Named(OKHTTP_REGULAR) private val api: HomeApiService,
    @Named(OKHTTP_WITHOUT_TOKEN) private val apiWithoutToken: HomeApiService
) : BaseRepository() {

    fun checkIfHasToken(): Boolean = appDataSource.checkIfHasToken()

    suspend fun getLyric(id: Long): NetResult<LyricPojo> = safeApiCallWithoutCode {
        api.lyric(buildingLyricParams(id.toString()))
    }

    suspend fun albumList(id: Long): NetResult<AlbumListPojo> = safeApiCallWithoutCode {
        api.albumList(buildingAlbumListParams(id.toString()))
    }

    suspend fun getSongUrl(id: Long): NetResult<Song> = safeApiCallWithoutCode {
        api.songUrl(buildingSongUrlParams(id.toString()))
    }

    suspend fun logout(): NetResult<Int> = safeApiCall {
        api.logout(buildingLogoutParams())
    }

    suspend fun syncAlbumListByUid(uid: String): NetResult<NetEase> = safeApiCallWithoutCode {
        api.syncAlbumList(buildingSyncAlbumListParams(uid))
    }

    suspend fun getAlbumCoverImgUrlById(id: String?): NetResult<AlbumCoverImageUrlPojo> =
        safeApiCallWithoutCode {
            api.getAlbumCoverImgUrlById(buildingAlbumCoverImgUrlParams(id))
        }

    suspend fun download(url: String): NetResult<String> {
        val response = apiWithoutToken.download(url)

        if (response.isSuccessful) {
            response.body()?.byteStream()?.apply {
                val file = createFileToDownLoadDir("mp3")
                file.copyInputStreamToFile(this)
                return NetResult.Success("下载成功: \n${file.absolutePath}")
            }
        }

        return NetResult.Error(
            ErrorException(
                response.code(),
                response.message()
            )
        )
    }

    private fun buildingSyncAlbumListParams(uid: String): Map<String, String> {
        val user = AppContext.get().appDataSource.user()
        return if (user?.displayName != null) {
            mapOf(
                "types" to "userlist",
                "uid" to uid,
                "username" to user.displayName!!
            )
        } else {
            mapOf(
                "types" to "userlist",
                "uid" to uid
            )
        }

    }

    private fun buildingAlbumCoverImgUrlParams(id: String?): Map<String, String?> {
        return mapOf(
            "types" to "pic",
            "id" to id,
            "source" to "netease"
        )
    }

    private fun buildingSongUrlParams(id: String): Map<String, String> {
        return mapOf(
            "types" to "url",
            "id" to id,
            "source" to "netease"
        )
    }

    private fun buildingLogoutParams(): Map<String, String?> {
        return mapOf(
            "token" to AppContext.appDataSource().appToken(),
            "types" to "logout"
        )
    }

    private fun buildingAlbumListParams(id: String): Map<String, String> {
        return mapOf(
            "types" to "playlist",
            "id" to id
        )
    }

    private fun buildingLyricParams(id: String): Map<String, String> {
        return mapOf(
            "types" to "lyric",
            "id" to id,
            "source" to "netease"
        )
    }
}