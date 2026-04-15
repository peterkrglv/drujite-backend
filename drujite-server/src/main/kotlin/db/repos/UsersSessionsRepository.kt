package db.repos

import models.SessionModel
import models.UserModel
import java.util.UUID

interface UsersSessionsRepository {
    suspend fun addUserSession(
        userId: UUID,
        sessionId: Int,
        characterId: Int? = null,
    ): Boolean

    suspend fun addCharacter(
        userId: UUID,
        sessionId: Int,
        characterId: Int,
        transferReason: String? = null,
    ): Boolean

    suspend fun deleteUserSession(
        userId: UUID,
        sessionId: Int,
    ): Boolean

    suspend fun deleteCharacter(
        userId: UUID,
        sessionId: Int,
        characterId: Int,
    ): Boolean

    suspend fun getCharacterId(
        userId: UUID,
        sessionId: Int,
    ): Int?

    suspend fun getCharacterIds(userId: UUID): List<Int>

    suspend fun getSessionsCharactersIds(sessionId: Int): List<Int>

    suspend fun getUsersSessions(userId: UUID): List<SessionModel>

    suspend fun getUserByCharacter(characterId: Int): UserModel?

    suspend fun getIdByCharacterAndSession(
        characterId: Int,
        sessionId: Int,
    ): Int?

    suspend fun getIdByUserAndSession(
        userId: UUID,
        sessionId: Int,
    ): Int?
}
