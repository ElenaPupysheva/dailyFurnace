package com.alonsonya.dailyfurnace.data.api

import com.alonsonya.dailyfurnace.BuildConfig
import retrofit2.http.Body
import retrofit2.http.POST



interface TokenApi {
    @POST("/push/register")
    suspend fun register(@Body body: RegisterPushReq)
}

data class RegisterPushReq(
    val token: String,
    val platform: String = "android",
    val appVersion: String = BuildConfig.APP_VERSION_NAME
)