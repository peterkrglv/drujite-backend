package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddCharactersClothingRequest(
    val characterId: Int,
    val itemsIds: List<Int>,
)
