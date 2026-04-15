package models

data class EventModel(
    val id: Int,
    val timetableId: Int,
    val name: String,
    val time: String,
    val isTitle: Boolean,
)
