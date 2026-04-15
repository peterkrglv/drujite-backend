package services

import db.repos.CharacterRepository
import db.repos.ClanRepository
import db.repos.UsersSessionsRepository
import models.CharacterModel
import responses.UserCharacterResponse

class CharacterService(
    private val characterRepository: CharacterRepository,
    private val usersSessionsRepository: UsersSessionsRepository,
    private val clanRepository: ClanRepository,
) {
    suspend fun getCharacter(id: Int) = characterRepository.get(id)

    suspend fun addCharacter(character: CharacterModel) = characterRepository.add(character)

    suspend fun deleteCharacter(id: Int) = characterRepository.delete(id)

    suspend fun getCharacterWithClanAndUser(id: Int): UserCharacterResponse? {
        val character = characterRepository.get(id)
        if (character != null) {
            val clan = clanRepository.get(character.clanId)
            val user = usersSessionsRepository.getUserByCharacter(character.id)
            if (clan != null && user != null) {
                return UserCharacterResponse(
                    id = character.id,
                    name = character.name,
                    player = user.username,
                    story = character.story,
                    clan = clan.name,
                    imageUrl = character.imageUrl,
                )
            }
        }
        return null
    }

    suspend fun changeCharactersStory(
        id: Int,
        newStory: String,
    ) = characterRepository.changeStory(id, newStory)
}
