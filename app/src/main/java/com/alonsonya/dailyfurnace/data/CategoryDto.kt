package com.alonsonya.dailyfurnace.data

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
data class ProductsResponse(
    val products: List<ProductDto>,
    val total: Int)