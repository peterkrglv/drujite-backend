package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClothingItemRequest(
    val name: String?,
    val typeId: Int,
    val imageUrl: String? = null,
    val iconUrl: String? = null,
)
