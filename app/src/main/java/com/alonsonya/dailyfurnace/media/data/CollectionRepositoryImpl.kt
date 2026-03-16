package com.alonsonya.dailyfurnace.media.data

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.data.db.dao.FurnaceDao
import com.alonsonya.dailyfurnace.data.mappers.toDomainDetails
import com.alonsonya.dailyfurnace.data.mappers.toEntity
import com.alonsonya.dailyfurnace.data.mappers.toEntityList
import com.alonsonya.dailyfurnace.data.mappers.toFurnaceItem
import com.alonsonya.dailyfurnace.data.repo.FurnacesRepository
import com.alonsonya.dailyfurnace.media.domain.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepositoryImpl(
    private val furnacesRepository: FurnacesRepository,
    private val furnaceDao: FurnaceDao
) : CollectionRepository {

    override fun observeFurnaces(): Flow<List<FurnaceItem>> {
        return furnaceDao.observeAll()
            .map { entities -> entities.map { it.toFurnaceItem() } }
    }

    override fun observeSearch(query: String): Flow<List<FurnaceItem>> {
        return furnaceDao.observeSearch(query)
            .map { entities -> entities.map { it.toFurnaceItem() } }
    }

    override fun observeFurnaceDetails(furnaceId: Int): Flow<Furnace?> {
        return furnaceDao.observeById(furnaceId)
            .map { entity -> entity?.toDomainDetails() }
    }

    override suspend fun syncFurnacesPage(limit: Int, offset: Int): Int {
        val dtos = furnacesRepository.getAllFurnaces(limit = limit, offset = offset)
        furnaceDao.insertAll(dtos.toEntityList())
        return dtos.size
    }

    override suspend fun syncFurnaceDetails(furnaceId: Int) {
        val dto = furnacesRepository.getFurnace(furnaceId)
        furnaceDao.insert(dto.toEntity())
    }
}