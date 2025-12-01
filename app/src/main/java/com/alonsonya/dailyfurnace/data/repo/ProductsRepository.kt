package com.alonsonya.dailyfurnace.data.repo

import com.alonsonya.dailyfurnace.data.ProductDto
import com.alonsonya.dailyfurnace.data.api.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ProductsRepository(private val api: ApiService) {

    suspend fun getProduct(id: Int): ProductDto =
        api.getProduct(id).normalizeUrl()

    suspend fun getFirstProductOrNull(): ProductDto? =
        api.getProducts().products.firstOrNull()?.normalizeUrl()

    suspend fun getByIds(ids: List<Int>): List<ProductDto> {
        if (ids.isEmpty()) return emptyList()
        val csv = ids.joinToString(",")
        return api.getProductsByIds(csv).map { it.normalizeUrl() }
    }

    suspend fun getByIdsFallback(ids: List<Int>): List<ProductDto> = coroutineScope {
        ids.map { async { api.getProduct(it).normalizeUrl() } }.awaitAll()
    }

    suspend fun getAllProducts(): List<ProductDto> =
        api.getProducts()
            .products
            .map { it.normalizeUrl() }

    private fun ProductDto.normalizeUrl(): ProductDto {
        val base = "https://furnace.eln.haswell668.ru"
        return if (!image_url.isNullOrBlank() && !image_url.startsWith("http"))
            copy(image_url = base + image_url) else this
    }
}