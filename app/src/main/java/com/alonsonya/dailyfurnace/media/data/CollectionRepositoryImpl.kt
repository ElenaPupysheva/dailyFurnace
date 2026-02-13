package com.alonsonya.dailyfurnace.media.data

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceDto
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.data.repo.FurnacesRepository
import com.alonsonya.dailyfurnace.media.domain.CollectionRepository

class CollectionRepositoryImpl(
    private val furnacesRepository: FurnacesRepository
) : CollectionRepository {

    override suspend fun getFurnace(furnaceId: Int): Furnace {
        val dto: FurnaceDto = furnacesRepository.getFurnace(furnaceId)
        return dto.toFurnaceDetails()
    }

    override suspend fun getFurnaceList(): List<Furnace> {
        val dtos: List<FurnaceDto> = furnacesRepository.getAllFurnaces()
        return dtos.map { it.toFurnaceListItem() }
    }

    override suspend fun getFurnacesPage(
        limit: Int,
        offset: Int
    ): List<FurnaceItem> {
        return furnacesRepository.getFurnacesPage(limit, offset)
    }

    private fun FurnaceDto.toFurnaceListItem(): Furnace =
        Furnace(
            furnaceId = id.toLong(),
            furnaceName = title,
            furnaceInfo = shortDescription,
            furnaceType = "",
            imageRes = imageUrl ?: thumbnailUrl
        )

    private fun FurnaceDto.toFurnaceDetails(): Furnace =
        Furnace(
            furnaceId = id.toLong(),
            furnaceName = title,
            furnaceInfo = fullDescription,
            furnaceType = "",
            imageRes = imageUrl ?: thumbnailUrl
        )
}
