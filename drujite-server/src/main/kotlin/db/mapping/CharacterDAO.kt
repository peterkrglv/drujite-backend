package db.mapping

import models.CharacterModel
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CharacterTable : IntIdTable("characters") {
    val name = varchar("name", DbStringLength.STANDARD)
    val story = text("story")
    val clanId = integer("clan_id").references(ClanTable.id)
    val image_url = varchar("image_url", DbStringLength.STANDARD).nullable()
}

class CharacterDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<CharacterDAO>(CharacterTable)

    var name by CharacterTable.name
    var story by CharacterTable.story
    var clanId by CharacterTable.clanId
    var imageUrl by CharacterTable.image_url
}

fun daoToModel(dao: CharacterDAO) =
    CharacterModel(
        id = dao.id.value,
        name = dao.name,
        story = dao.story,
        clanId = dao.clanId,
        imageUrl = dao.imageUrl,
    )
