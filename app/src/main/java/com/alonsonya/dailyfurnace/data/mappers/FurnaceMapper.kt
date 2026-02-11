package com.alonsonya.dailyfurnace.data.mappers

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceDto

fun FurnaceDto.toDomain(
    isFavorite: Boolean = false,
    typeFallback: String = ""
): Furnace = Furnace(
    furnaceId = id.toLong(),
    furnaceName = title,
    furnaceInfo = shortDescription,
    furnaceType = typeFallback,
    imageRes = imageUrl ?: thumbnailUrl,
    isFavorite = isFavorite
)

fun FurnaceDto.toDomainDetails(
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
