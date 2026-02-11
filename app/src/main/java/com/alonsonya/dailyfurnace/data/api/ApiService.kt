package com.alonsonya.dailyfurnace.data.api

import com.alonsonya.dailyfurnace.data.FurnaceDto
import com.alonsonya.dailyfurnace.data.FurnacesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("furnaces")
    suspend fun getFurnaces(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): FurnacesResponse

    @GET("furnaces/daily")
    suspend fun getDailyFurnace(): FurnaceDto

    @GET("furnaces/{id}")
    suspend fun getFurnace(@Path("id") id: Int): FurnaceDto
}