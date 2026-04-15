package db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object SessionsClansTable : IntIdTable("sessions_clans") {
    val sessionId = integer("session_id").references(SessionTable.id)
    val clanId = integer("clan_id").references(ClanTable.id)
}

class SessionClansDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<SessionClansDAO>(SessionsClansTable)

    var sessionId by SessionsClansTable.sessionId
    var clanId by SessionsClansTable.clanId
}
