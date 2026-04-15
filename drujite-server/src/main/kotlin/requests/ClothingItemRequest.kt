package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClothingItemRequest(
    val name: String?,
    val typeId: Int,
    val iconUrl: String? = null,
    val imageUrl: String? = null,
)
