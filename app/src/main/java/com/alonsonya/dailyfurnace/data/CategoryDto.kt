package com.alonsonya.dailyfurnace.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Int,
    val name: String,
    val slug: String
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val category_id: Int,
    val image_url: String?,
    val created_at: String,
    val category: CategoryDto
)

@JsonClass(generateAdapter = true)
data class FurnaceDto(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val imageUrl: String?,
    @Json(name = "tumbnaillUrl")
    val thumbnailUrl: String?
)

@JsonClass(generateAdapter = true)
data class FurnacesResponse(
    val furnaces: List<FurnaceDto>
)

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    val products: List<ProductDto>,
    val total: Int
)