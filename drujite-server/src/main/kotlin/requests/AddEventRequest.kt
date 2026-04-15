package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddEventRequest(
    val timetableId: Int,
    val name: String,
    val time: String,
    val isTitle: Boolean = false,
)
