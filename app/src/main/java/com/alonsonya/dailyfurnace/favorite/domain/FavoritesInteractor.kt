package com.alonsonya.dailyfurnace.favorite.domain

import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun add(id: Int)
    suspend fun remove(id: Int)
    suspend fun toggle(id: Int): Boolean
    fun isFavoriteFlow(id: Int): Flow<Boolean>
    suspend fun isFavorite(id: Int): Boolean
    fun observeAllIds(): Flow<List<Long>>
}