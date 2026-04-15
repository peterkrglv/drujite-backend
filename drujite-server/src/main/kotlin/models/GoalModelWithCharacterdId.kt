package ru.drujite.models

data class GoalModelWithCharacterdId(
    val id: Int,
    val characterId: Int,
    val name: String,
    val isCompleted: Boolean,
)
