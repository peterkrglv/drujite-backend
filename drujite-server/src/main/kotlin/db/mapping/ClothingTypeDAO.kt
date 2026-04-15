package db.mapping

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ClothingTypeTable : IntIdTable("clothing_type") {
    val name = varchar("name", DbStringLength.STANDARD)
    val isEditable = bool("iseditable").default(true)
}

class ClothingTypeDAO(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<ClothingTypeDAO>(ClothingTypeTable)

    var name by ClothingTypeTable.name
    var isEditable by ClothingTypeTable.isEditable
}
