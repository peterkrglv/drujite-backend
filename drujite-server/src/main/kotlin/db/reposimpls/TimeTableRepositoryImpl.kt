package db.reposimpls

import db.mapping.TimeTable
import db.mapping.TimeTableDAO
import db.mapping.suspendTransaction
import db.repos.TimeTableRepository
import models.TimeTableModel
import org.jetbrains.exposed.sql.and

class TimeTableRepositoryImpl : TimeTableRepository {
    override suspend fun add(timeTable: TimeTableModel): Int =
        suspendTransaction {
            TimeTableDAO
                .new {
                    sessionId = timeTable.sessionId
                    date = java.time.LocalDate.parse(timeTable.date)
                }.id.value
        }

    override suspend fun delete(timeTableId: Int): Boolean =
        suspendTransaction {
            TimeTableDAO.findById(timeTableId)?.delete() != null
        }

    override suspend fun getSessionsTimetables(sessionId: Int): List<TimeTableModel> =
        suspendTransaction {
            TimeTableDAO
                .find { TimeTable.sessionId eq sessionId }
                .map {
                    it.let {
                        TimeTableModel(
                            id = it.id.value,
                            sessionId = it.sessionId,
                            date = it.date.toString(),
                        )
                    }
                }
        }

    override suspend fun getBySessionAndDate(timeTableId: Int): TimeTableModel? =
        suspendTransaction {
            TimeTableDAO
                .findById(timeTableId)
                ?.let {
                    TimeTableModel(
                        id = it.id.value,
                        sessionId = it.sessionId,
                        date = it.date.toString(),
                    )
                }
        }

    override suspend fun getBySessionAndDate(
        sessionId: Int,
        date: String,
    ): TimeTableModel? =
        suspendTransaction {
            TimeTableDAO
                .find { (TimeTable.sessionId eq sessionId) and (TimeTable.date eq java.time.LocalDate.parse(date)) }
                .firstOrNull()
                ?.let {
                    TimeTableModel(
                        id = it.id.value,
                        sessionId = it.sessionId,
                        date = it.date.toString(),
                    )
                }
        }
}
