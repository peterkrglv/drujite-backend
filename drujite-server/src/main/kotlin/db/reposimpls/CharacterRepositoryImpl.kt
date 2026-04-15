package db.reposimpls

import db.mapping.CharacterDAO
import db.mapping.suspendTransaction
import db.repos.CharacterRepository
import models.CharacterModel

class CharacterRepositoryImpl : CharacterRepository {
    override suspend fun add(character: CharacterModel): Int =
        suspendTransaction {
            CharacterDAO
                .new {
                    name = character.name
                    story = character.story
                    clanId = character.clanId
                    imageUrl = character.imageUrl
                }.id.value
        }

    override suspend fun get(id: Int): CharacterModel? =
        suspendTransaction {
            CharacterDAO.findById(id)?.let {
                CharacterModel(
                    id = it.id.value,
                    name = it.name,
                    story = it.story,
                    clanId = it.clanId,
                    imageUrl = it.imageUrl,
                )
            }
        }

    override suspend fun delete(id: Int): Boolean =
        suspendTransaction {
            CharacterDAO.findById(id)?.delete() != null
        }

    override suspend fun addImageUrl(
        id: Int,
        imageUrl: String,
    ): Boolean =
        suspendTransaction {
            CharacterDAO.findById(id)?.let {
                it.imageUrl = imageUrl
                true
            } ?: false
        }

    override suspend fun changeStory(
        id: Int,
        story: String,
    ): Boolean =
        suspendTransaction {
            CharacterDAO.findById(id)?.let {
                it.story = story
                true
            } ?: false
        }
}
