package ru.drujite.requests

import kotlinx.serialization.Serializable

@Serializable
data class ChangeCharactersStoryRequest(
    val characterId: Int,
    val story: String,
)
