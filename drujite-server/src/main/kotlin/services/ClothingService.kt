package ru.drujite.services

import ru.drujite.db.repos.ClothingRepository
import ru.drujite.models.ClothingItem
import ru.drujite.models.ClothingType
import ru.drujite.models.ClothingTypeWithItems

class ClothingService(
    private val clothingRepository: ClothingRepository,
) {
    suspend fun addClothingType(clothingType: ClothingType): Int = clothingRepository.addClothingType(clothingType)

    suspend fun deleteClothingType(id: Int): Boolean = clothingRepository.deleteClothingType(id)

    suspend fun getClothingTypes(): List<ClothingType> = clothingRepository.getClothingTypes()

    suspend fun addClothingItem(clothingItem: ClothingItem): Int = clothingRepository.addClothingItem(clothingItem)

    suspend fun addImageUrl(
        id: Int,
        imageUrl: String,
    ): Boolean = clothingRepository.addImageUrl(id, imageUrl)

    suspend fun addIconUrl(
        id: Int,
        iconUrl: String,
    ): Boolean = clothingRepository.addIconUrl(id, iconUrl)

    suspend fun deleteClothingItem(id: Int): Boolean = clothingRepository.deleteClothingItem(id)

    suspend fun addClothingItemsToCharacter(
        characterId: Int,
        itemsIds: List<Int>,
    ): Boolean = clothingRepository.addClothingItemsToCharacter(characterId, itemsIds)

    suspend fun getCharactersClothing(characterId: Int): List<ClothingItem> = clothingRepository.getCharactersClothingItems(characterId)

    suspend fun getAllClothingItems(): List<ClothingTypeWithItems> = clothingRepository.getAllClothingItems()

    suspend fun getCharactersEditableClothingItems(characterId: Int): List<ClothingItem> =
        clothingRepository.getCharactersEditableClothingItems(characterId)
}
