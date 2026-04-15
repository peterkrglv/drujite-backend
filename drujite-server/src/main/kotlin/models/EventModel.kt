package models

data class EventModel(
    val id: Int,
    val isTitle: Boolean,
    val name: String,
    val time: String,
    val timetableId: Int,
)
