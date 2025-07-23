package com.alonsonya.dailyfurnace.data

const val SWITCH_KEY = "dark_theme"
const val EXTRA_FURNACE = "furnace"
const val FURNACE_PREFERENCES = "furnace_preferences"
val mockFurnaces = listOf(
    Furnace(1, "Классический камин", "Традиционный дизайн", null, true),
    Furnace(2, "Русская печь", "Для настоящих ценителей", null, false),
    Furnace(3, "Современный портал", "Хай-тек стиль", null, true)
)