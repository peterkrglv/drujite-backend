package db.mapping

import models.NewsModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object NewsTable : IntIdTable("news") {
    val sessionId = integer("session_id").references(SessionTable.id)
    val title = varchar("title", DbStringLength.STANDARD)
    val content = text("content")
    val time = timestamp("time")
    val imageUrl = varchar("image_url", DbStringLength.STANDARD).nullable()
}

class NewsDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<NewsDAO>(NewsTable)

    var sessionId by NewsTable.sessionId
    var title by NewsTable.title
    var content by NewsTable.content
    var time by NewsTable.time
    var imageUrl by NewsTable.imageUrl
}

fun daoToModel(dao: NewsDAO) =
    NewsModel(
        content = dao.content,
        dateTime = dao.time.toString(),
        id = dao.id.value,
        imageUrl = dao.imageUrl,
        sessionId = dao.sessionId,
        title = dao.title,
    )
