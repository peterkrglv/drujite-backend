package responses

import kotlinx.serialization.Serializable

@Serializable
data class GoalResponse(
    val id: Int,
    val characterId: Int,
    val name: String,
    val isCompleted: Boolean,
)
