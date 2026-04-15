package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class ClothingTypeResponse(
    val id: Int,
    val isEditable: Boolean,
    val name: String,
)
