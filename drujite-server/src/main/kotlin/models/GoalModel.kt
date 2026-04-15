package models

data class GoalModel(
    val id: Int,
    val usersSessionId: Int,
    val name: String,
    val isCompleted: Boolean,
)
