package db.repos

import models.TimeTableModel

interface TimeTableRepository {
    suspend fun add(timeTable: TimeTableModel): Int

    suspend fun delete(timeTableId: Int): Boolean

    suspend fun getSessionsTimetables(sessionId: Int): List<TimeTableModel>

    suspend fun getBySessionAndDate(timeTableId: Int): TimeTableModel?

    suspend fun getBySessionAndDate(
        sessionId: Int,
        date: String,
    ): TimeTableModel?
}
