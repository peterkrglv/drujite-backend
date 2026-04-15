package db.mapping

import models.TimeTableModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object TimeTable : IntIdTable("timetables") {
    val sessionId = integer("session_id").references(SessionTable.id)
    val date = date("date")
}

class TimeTableDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<TimeTableDAO>(TimeTable)

    var sessionId by TimeTable.sessionId
    var date by TimeTable.date
}

fun daoToModel(dao: TimeTableDAO) =
    TimeTableModel(
        id = dao.id.value,
        sessionId = dao.sessionId,
        date = dao.date.toString(),
    )
