package com.app.base.data.app

import com.app.base.data.models.Song
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AppApiService {

    @GET("/backend/login.php")
    suspend fun smsCode(@QueryMap loginParams: Map<String, String>): retrofit2.Response<String>

    @GET("/backend/api.php")
    suspend fun songUrl(@QueryMap params: Map<String, String>): retrofit2.Response<Song>
}

