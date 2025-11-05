package com.alonsonya.dailyfurnace.favorite.data

import com.alonsonya.dailyfurnace.data.db.dao.FavoriteFurnaceDao
import com.alonsonya.dailyfurnace.data.db.entity.FavoriteFurnaceEntity
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val dao: FavoriteFurnaceDao
) : FavoritesRepository {

    override suspend fun add(id: Int) { dao.insert(FavoriteFurnaceEntity(id)) }
    override suspend fun remove(id: Int) { dao.deleteById(id) }

    override suspend fun toggle(id: Int): Boolean =
        if (dao.isFavorite(id)) { dao.deleteById(id); false }
        else { dao.insert(FavoriteFurnaceEntity(id)); true }

    override fun isFavoriteFlow(id: Int): Flow<Boolean> = dao.isFavoriteFlow(id)
    override suspend fun isFavorite(id: Int): Boolean = dao.isFavorite(id)
    override fun observeAllIds(): Flow<List<Int>> = dao.observeAllIds()
}