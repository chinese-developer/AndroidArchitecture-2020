package com.example.architecture.home

import com.app.base.data.api.HttpResult
import com.app.base.data.models.NetEase
import com.app.base.data.models.Song
import com.example.architecture.home.repository.pojo.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface HomeApiService {

    @GET
    suspend fun download(@Url url: String): retrofit2.Response<ResponseBody>

    @GET("/backend/api.php")
    suspend fun albumList(@QueryMap params: Map<String, String>): retrofit2.Response<AlbumListPojo>

    @GET("/backend/api.php")
    suspend fun songUrl(@QueryMap params: Map<String, String>): retrofit2.Response<Song>

    @GET("/backend/api.php")
    suspend fun lyric(@QueryMap params: Map<String, String>): retrofit2.Response<LyricPojo>

    @POST("/backend/login.php")
    @FormUrlEncoded
    suspend fun logout(@FieldMap logoutParams: Map<String, String?>): retrofit2.Response<HttpResult<Int>>

    @GET("/backend/api.php")
    suspend fun search(@QueryMap params: Map<String, String>): retrofit2.Response<List<SearchListPojo>>

    @GET("/backend/api.php")
    suspend fun syncAlbumList(@QueryMap params: Map<String, String>): retrofit2.Response<NetEase>

    @GET("/backend/api.php")
    suspend fun getAlbumCoverImgUrlById(@QueryMap buildingAlbumCoverImgUrlParams: Map<String, String?>): retrofit2.Response<AlbumCoverImageUrlPojo>
}