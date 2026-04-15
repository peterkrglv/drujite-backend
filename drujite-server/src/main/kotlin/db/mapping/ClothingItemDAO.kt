package db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ClothingItemTable : IntIdTable("clothing_item") {
    val name = varchar("name", DbStringLength.STANDARD).nullable()
    val typeId = integer("type_id").references(ClothingTypeTable.id)
    val imageUrl = varchar("image_url", DbStringLength.STANDARD).nullable()
    val iconImageUrl = varchar("image_icon_url", DbStringLength.STANDARD).nullable()
}

class ClothingItemDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ClothingItemDAO>(ClothingItemTable)

    var name by ClothingItemTable.name
    var typeId by ClothingItemTable.typeId
    var imageUrl by ClothingItemTable.imageUrl
    var iconImageUrl by ClothingItemTable.iconImageUrl
}
