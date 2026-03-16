package com.alonsonya.dailyfurnace.data

data class FurnaceItem(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val imageUrl: String?,
    val thumbnailUrl: String?
)