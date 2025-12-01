package com.alonsonya.dailyfurnace.media.data

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.ProductDto
import com.alonsonya.dailyfurnace.data.repo.ProductsRepository
import com.alonsonya.dailyfurnace.media.domain.CollectionRepository

class CollectionRepositoryImpl(private val productsRepository: ProductsRepository
) : CollectionRepository {

    override suspend fun getFurnace(furnaceId: Int): Furnace {

        val dto: ProductDto = productsRepository.getProduct(furnaceId)
        return dto.toFurnace()
    }

    override suspend fun getFurnaceList(): List<Furnace> {
        val dtos: List<ProductDto> = productsRepository.getAllProducts()
        return dtos.map { it.toFurnace() }
    }
    private fun ProductDto.toFurnace(): Furnace =
        Furnace(
            furnaceId = id.toLong(),
            furnaceName = name,
            furnaceInfo = description.orEmpty(),
            furnaceType = category.name,
            imageRes = image_url
        )
}
