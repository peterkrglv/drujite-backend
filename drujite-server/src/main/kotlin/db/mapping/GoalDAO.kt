package db.mapping

import models.GoalModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object GoalTable : IntIdTable("goals") {
    val usersSessionId = integer("users_session_id").references(UsersSessionsTable.id)
    val name = varchar("name", DbStringLength.STANDARD)
    val isCompleted = bool("is_completed").default(false)
}

class GoalDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<GoalDAO>(GoalTable)

    var usersSessionId by GoalTable.usersSessionId
    var name by GoalTable.name
    var isCompleted by GoalTable.isCompleted
}

fun daoToModel(dao: GoalDAO) =
    GoalModel(
        id = dao.id.value,
        isCompleted = dao.isCompleted,
        name = dao.name,
        usersSessionId = dao.usersSessionId,
    )
