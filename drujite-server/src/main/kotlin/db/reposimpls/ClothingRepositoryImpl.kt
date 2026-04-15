package db.reposimpls

import db.mapping.CharacterClothingDAO
import db.mapping.CharacterClothingTable
import db.mapping.ClothingItemDAO
import db.mapping.ClothingItemTable
import db.mapping.ClothingTypeDAO
import db.mapping.ClothingTypeTable
import db.mapping.suspendTransaction
import ru.drujite.db.repos.ClothingRepository
import ru.drujite.models.ClothingItem
import ru.drujite.models.ClothingType
import ru.drujite.models.ClothingTypeWithItems

class ClothingRepositoryImpl : ClothingRepository {
    override suspend fun addClothingType(clothingType: ClothingType): Int =
        suspendTransaction {
            ClothingTypeDAO
                .new {
                    this.isEditable = clothingType.isEditable
                    this.name = clothingType.name
                }.id.value
        }

    override suspend fun deleteClothingType(id: Int): Boolean =
        suspendTransaction {
            ClothingTypeDAO.findById(id)?.delete() != null
        }

    override suspend fun getClothingTypes(): List<ClothingType> =
        suspendTransaction {
            ClothingTypeDAO.all().map {
                ClothingType(
                    id = it.id.value,
                    isEditable = it.isEditable,
                    name = it.name,
                )
            }
        }

    override suspend fun getEditableClothingTypes(): List<ClothingType> =
        suspendTransaction {
            ClothingTypeDAO.find { ClothingTypeTable.isEditable eq true }.map {
                ClothingType(
                    id = it.id.value,
                    isEditable = it.isEditable,
                    name = it.name,
                )
            }
        }

    override suspend fun addClothingItem(clothingItem: ClothingItem): Int =
        suspendTransaction {
            ClothingItemDAO
                .new {
                    this.name = clothingItem.name
                    this.typeId = clothingItem.typeId
                }.id.value
        }

    override suspend fun addImageUrl(
        clothingItemId: Int,
        imageUrl: String,
    ): Boolean =
        suspendTransaction {
            ClothingItemDAO.findById(clothingItemId)?.let {
                it.imageUrl = imageUrl
                it.flush()
                true
            } == true
        }

    override suspend fun addIconUrl(
        clothingItemId: Int,
        iconUrl: String,
    ): Boolean =
        suspendTransaction {
            ClothingItemDAO.findById(clothingItemId)?.let {
                it.iconImageUrl = iconUrl
                it.flush()
                true
            } ?: false
        }

    override suspend fun getClothingItemType(id: Int): Int? =
        suspendTransaction {
            ClothingItemDAO.findById(id)?.typeId
        }

    override suspend fun deleteClothingItem(id: Int): Boolean =
        suspendTransaction {
            ClothingItemDAO.findById(id)?.delete() != null
        }

    override suspend fun addClothingItemsToCharacter(
        characterId: Int,
        itemsIds: List<Int>,
    ): Boolean =
        suspendTransaction {
            CharacterClothingDAO.find { CharacterClothingTable.characterId eq characterId }.forEach {
                it.delete()
            }
            itemsIds.forEach { itemId ->
                CharacterClothingDAO.new {
                    this.characterId = characterId
                    this.clothingItemId = itemId
                }
            }
            true
        }

    override suspend fun getCharactersClothingItems(characterId: Int): List<ClothingItem> {
        return suspendTransaction {
            CharacterClothingDAO.find { CharacterClothingTable.characterId eq characterId }.mapNotNull {
                val clothingItem = it.clothingItemId ?: return@mapNotNull null
                ClothingItemDAO.findById(clothingItem)?.let { item ->
                    ClothingItem(
                        iconUrl = item.iconImageUrl,
                        id = item.id.value,
                        imageUrl = item.imageUrl,
                        name = item.name,
                        typeId = item.typeId,
                    )
                }
            }
        }
    }

    override suspend fun getAllClothingItems(): List<ClothingTypeWithItems> =
        suspendTransaction {
            ClothingTypeDAO.all().map { type ->
                val items =
                    ClothingItemDAO.find { ClothingItemTable.typeId eq type.id.value }.map {
                        ClothingItem(
                            iconUrl = it.iconImageUrl,
                            id = it.id.value,
                            imageUrl = it.imageUrl,
                            name = it.name,
                            typeId = it.typeId,
                        )
                    }
                ClothingTypeWithItems(
                    id = type.id.value,
                    isEditable = type.isEditable,
                    items = items,
                    name = type.name,
                )
            }
        }

    override suspend fun getCharactersEditableClothingItems(characterId: Int): List<ClothingItem> {
        return suspendTransaction {
            CharacterClothingDAO.find { CharacterClothingTable.characterId eq characterId }.mapNotNull {
                val clothingItem = it.clothingItemId ?: return@mapNotNull null
                ClothingItemDAO.findById(clothingItem)?.let { item ->
                    val type = ClothingTypeDAO.findById(item.typeId)
                    if (type?.isEditable == true) {
                        ClothingItem(
                            iconUrl = item.iconImageUrl,
                            id = item.id.value,
                            imageUrl = item.imageUrl,
                            name = item.name,
                            typeId = item.typeId,
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }
}
