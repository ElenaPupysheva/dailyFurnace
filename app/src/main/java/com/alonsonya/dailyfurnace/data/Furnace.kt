package com.alonsonya.dailyfurnace.data

data class Furnace(
    val furnaceId: Int,
    val furnaceName: String,
    val furnaceInfo: String,
    val imageRes: String?,
    var isFavorite: Boolean = false
)