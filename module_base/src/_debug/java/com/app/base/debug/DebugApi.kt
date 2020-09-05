package com.app.base.debug

import com.app.base.data.api.HttpResult
import com.app.base.data.models.LoggedInUser
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface DebugApi {

    @POST("/backend/login.php")
    @FormUrlEncoded
    suspend fun login(@FieldMap loginParams: Map<String, String>): HttpResult<LoggedInUser>

}

