package ru.drujite.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothingItem(
    val iconUrl: String?,
    val id: Int,
    val imageUrl: String?,
    val name: String?,
    val typeId: Int,
)
