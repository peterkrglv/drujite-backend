package db.mapping

import models.SessionModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object SessionTable : IntIdTable("sessions") {
    val name = varchar("name", DbStringLength.STANDARD)
    val description = text("description")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val imageUrl = varchar("image_url", DbStringLength.STANDARD).nullable()
    val qr = varchar("qr", DbStringLength.STANDARD).nullable()
}

class SessionDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<SessionDAO>(SessionTable)

    var name by SessionTable.name
    var description by SessionTable.description
    var startDate by SessionTable.startDate
    var endDate by SessionTable.endDate
    var imageUrl by SessionTable.imageUrl
    var qr by SessionTable.qr
}

fun daoToModel(dao: SessionDAO) =
    SessionModel(
        id = dao.id.value,
        name = dao.name,
        description = dao.description,
        startDate = dao.startDate.toString(),
        endDate = dao.endDate.toString(),
        imageUrl = dao.imageUrl,
        qrLink = dao.qr,
    )
