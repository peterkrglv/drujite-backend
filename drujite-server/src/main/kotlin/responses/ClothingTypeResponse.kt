package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class ClothingTypeResponse(
    val id: Int,
    val name: String,
    val isEditable: Boolean,
)
