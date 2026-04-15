package models

data class GoalModel(
    val id: Int,
    val isCompleted: Boolean,
    val name: String,
    val usersSessionId: Int,
)
