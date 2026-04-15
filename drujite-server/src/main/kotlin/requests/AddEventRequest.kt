package requests

import kotlinx.serialization.Serializable

@Serializable
data class AddEventRequest(
    val name: String,
    val time: String,
    val timetableId: Int,
    val isTitle: Boolean = false,
)
