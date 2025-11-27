package com.alonsonya.dailyfurnace.media.domain

import androidx.lifecycle.LiveData
import com.alonsonya.dailyfurnace.data.Furnace

interface CollectionInteractor {
    fun getFurnace(furnaceId: Int): Furnace
    fun getFurnaceList(): LiveData<List<Furnace>>
}