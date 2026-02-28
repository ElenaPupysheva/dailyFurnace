package com.alonsonya.dailyfurnace.data.repo

import com.alonsonya.dailyfurnace.data.FurnaceDto
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.data.api.ApiService

class FurnacesRepository(private val api: ApiService) {

    suspend fun getFurnace(id: Int): FurnaceDto =
        api.getFurnace(id).normalizeUrl()

    suspend fun getDailyFurnace(): FurnaceDto =
        api.getDailyFurnace().normalizeUrl()

    suspend fun getAllFurnaces(limit: Int? = null, offset: Int? = null): List<FurnaceDto> =
        api.getFurnaces(query = null, limit = limit, offset = offset)
            .furnaces
            .map { it.normalizeUrl() }


    suspend fun getFurnacesPage(limit: Int, offset: Int): List<FurnaceItem> {
        return api.getFurnaces(query = null, limit = limit, offset = offset)
            .furnaces
            .map { it.normalizeUrl() }
            .map { dto ->
                FurnaceItem(
                    id = dto.id,
                    title = dto.title,
                    shortDescription = dto.shortDescription,
                    imageUrl = dto.imageUrl,
                    thumbnailUrl = dto.thumbnailUrl
                )
            }
    }

    suspend fun searchFurnacesPage(query: String, limit: Int, offset: Int): List<FurnaceItem> {
        return api.getFurnaces(query, limit, offset)
            .furnaces
            .map { it.normalizeUrl() }
            .map { dto ->
                FurnaceItem(
                    id = dto.id,
                    title = dto.title,
                    shortDescription = dto.shortDescription,
                    imageUrl = dto.imageUrl,
                    thumbnailUrl = dto.thumbnailUrl
                )
            }
    }

    private fun FurnaceDto.normalizeUrl(): FurnaceDto {
        val base = "http://149.154.71.181:6060"

        fun String?.abs(): String? =
            if (this.isNullOrBlank()) null
            else if (this.startsWith("http")) this
            else base + this

        return copy(
            imageUrl = imageUrl.abs(),
            thumbnailUrl = thumbnailUrl.abs()
        )
    }
}
