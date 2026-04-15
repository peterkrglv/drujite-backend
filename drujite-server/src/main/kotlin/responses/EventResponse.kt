package responses

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val id: Int,
    val isTitle: Boolean,
    val name: String,
    val time: String?,
    val timetableId: Int,
)
