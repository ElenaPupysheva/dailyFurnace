package com.alonsonya.dailyfurnace.data.api

import com.alonsonya.dailyfurnace.data.ProductDto
import com.alonsonya.dailyfurnace.data.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products/by_ids")
    suspend fun getProductsByIds(@Query("ids") idsCsv: String): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto
}