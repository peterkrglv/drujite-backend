package ru.drujite.models

data class GoalModelWithCharacterdId(
    val characterId: Int,
    val id: Int,
    val isCompleted: Boolean,
    val name: String,
)
