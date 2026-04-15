package ru.drujite.responses

import kotlinx.serialization.Serializable

@Serializable
data class GoalModelWithCharacterIdResponse(
    val characterId: Int,
    val id: Int,
    val isCompleted: Boolean,
    val name: String,
)
