package db.repos

import models.EventModel

interface EventRepository {
    suspend fun add(event: EventModel): Int?

    suspend fun get(id: Int): EventModel?

    suspend fun delete(id: Int): Boolean

    suspend fun getTimetableEventIds(timetableId: Int): List<Int>
}
