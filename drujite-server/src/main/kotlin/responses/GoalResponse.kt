package responses

import kotlinx.serialization.Serializable

@Serializable
data class GoalResponse(
    val characterId: Int,
    val id: Int,
    val isCompleted: Boolean,
    val name: String,
)
