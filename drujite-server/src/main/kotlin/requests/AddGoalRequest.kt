package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddGoalRequest(
    val characterId: Int,
    val name: String,
    val sessionId: Int,
    val isCompleted: Boolean = false,
)
