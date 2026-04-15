package ru.drujite.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothingTypeWithItems(
    val id: Int,
    val isEditable: Boolean,
    val items: List<ClothingItem>,
    val name: String,
)
