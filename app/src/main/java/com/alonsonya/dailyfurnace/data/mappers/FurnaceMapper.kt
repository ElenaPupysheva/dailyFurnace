package com.alonsonya.dailyfurnace.data.mappers

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceDto
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.data.db.entity.FurnaceEntity

fun FurnaceDto.toEntity(): FurnaceEntity = FurnaceEntity(
    id = id,
    title = title,
    shortDescription = shortDescription,
    fullDescription = fullDescription,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl
)

fun List<FurnaceDto>.toEntityList(): List<FurnaceEntity> = map { it.toEntity() }

fun FurnaceEntity.toFurnaceItem(): FurnaceItem = FurnaceItem(
    id = id,
    title = title,
    shortDescription = shortDescription,
    imageUrl = imageUrl,
    thumbnailUrl = thumbnailUrl
)

fun FurnaceEntity.toDomainDetails(
    isFavorite: Boolean = false,
    typeFallback: String = ""
): Furnace = Furnace(
    furnaceId = id.toLong(),
    furnaceName = title,
    furnaceInfo = fullDescription,
    furnaceType = typeFallback,
    imageRes = imageUrl ?: thumbnailUrl,
    isFavorite = isFavorite
)