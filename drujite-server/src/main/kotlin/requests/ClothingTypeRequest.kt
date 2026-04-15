package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class ClothingTypeRequest(
    val name: String,
    val isEditable: Boolean,
)
