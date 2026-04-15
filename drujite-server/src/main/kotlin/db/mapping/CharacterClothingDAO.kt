package db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CharacterClothingTable : IntIdTable("character_clothing") {
    val characterId = integer("character_id").references(CharacterTable.id).nullable()
    val clothingItemId = integer("clothing_item_id").references(ClothingItemTable.id).nullable()
}

class CharacterClothingDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<CharacterClothingDAO>(CharacterClothingTable)

    var characterId by CharacterClothingTable.characterId
    var clothingItemId by CharacterClothingTable.clothingItemId
}
