package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClothingTypeRequest(
    val isEditable: Boolean,
    val name: String,
)
