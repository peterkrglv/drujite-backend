package db.repos

import models.CharacterModel

interface CharacterRepository {
    suspend fun add(character: CharacterModel): Int

    suspend fun get(id: Int): CharacterModel?

    suspend fun delete(id: Int): Boolean

    suspend fun addImageUrl(
        id: Int,
        imageUrl: String,
    ): Boolean

    suspend fun changeStory(
        id: Int,
        story: String,
    ): Boolean
}
