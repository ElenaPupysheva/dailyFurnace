package com.alonsonya.dailyfurnace.data

data class Furnace(
    val furnaceId: Long,
    val furnaceName: String,
    val furnaceInfo: String,
    val furnaceType: String,
    val imageRes: String?,
    var isFavorite: Boolean = false
)