package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class GoalModelWithCharacterIdResponse(
    val id: Int,
    val characterId: Int,
    val name: String,
    val isCompleted: Boolean,
)
