package com.alonsonya.dailyfurnace.media.domain

import androidx.lifecycle.LiveData
import com.alonsonya.dailyfurnace.data.Furnace

interface CollectionRepository {
    fun getFurnace(furnaceId: Int): Furnace
    fun getFurnaceList(): LiveData<List<Furnace>>
}