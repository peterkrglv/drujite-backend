package services

import db.repos.CharacterRepository
import db.repos.SessionRepository
import db.repos.UsersSessionsRepository
import models.CharacterModel
import models.SessionModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.drujite.util.getRedirectedUrl
import java.util.UUID

class UsersSessionsService(
    private val usersSessionsRepository: UsersSessionsRepository,
    private val characterRepository: CharacterRepository,
    private val sessionRepository: SessionRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(UsersSessionsService::class.java)

    suspend fun addUserSession(
        userId: String,
        sessionId: Int,
        characterId: Int? = null,
    ) = usersSessionsRepository.addUserSession(
        userId = UUID.fromString(userId),
        sessionId = sessionId,
        characterId = characterId,
    )

    suspend fun addCharacter(
        userId: String,
        sessionId: Int,
        characterId: Int,
    ) = usersSessionsRepository.addCharacter(
        userId = UUID.fromString(userId),
        sessionId = sessionId,
        characterId = characterId,
    )

    suspend fun deleteUserSession(
        userId: String,
        sessionId: Int,
    ) = usersSessionsRepository.deleteUserSession(
        userId = UUID.fromString(userId),
        sessionId = sessionId,
    )

    suspend fun deleteCharacter(
        userId: String,
        sessionId: Int,
        characterId: Int,
    ) = usersSessionsRepository.deleteCharacter(
        userId = UUID.fromString(userId),
        sessionId = sessionId,
        characterId = characterId,
    )

    suspend fun getCharacter(
        userId: String,
        sessionId: Int,
    ): CharacterModel? {
        val characterId =
            usersSessionsRepository.getCharacterId(
                userId = UUID.fromString(userId),
                sessionId = sessionId,
            )
        return characterId?.let { characterRepository.get(it) }
    }

    suspend fun getCharacters(userId: String): List<CharacterModel> {
        val characterIds =
            usersSessionsRepository.getCharacterIds(
                userId = UUID.fromString(userId),
            )
        return characterIds.mapNotNull { characterRepository.get(it) }
    }

    suspend fun getUsersSessions(userId: String) =
        usersSessionsRepository.getUsersSessions(
            userId = UUID.fromString(userId),
        )

    suspend fun getSessionsCharacters(sessionId: Int): List<CharacterModel> {
        val characterIds = usersSessionsRepository.getSessionsCharactersIds(sessionId)
        return characterIds.mapNotNull { characterRepository.get(it) }
    }

    suspend fun addUsersSessionByQr(
        userId: String,
        qr: String,
    ): SessionModel? {
        logger.info("Adding user session: $qr")
        var session = sessionRepository.getSessionByQr(qr)
        logger.info("Session: $session")
        if (session == null) {
            val redirectedUrl = getRedirectedUrl(qr)
            logger.info("Redirect: $redirectedUrl")
            session =
                redirectedUrl?.let {
                    sessionRepository.getSessionByQr(it)
                }
        }
        logger.info("Session after redirect: $session")
        return session?.let {
            usersSessionsRepository.addUserSession(
                userId = UUID.fromString(userId),
                sessionId = session.id,
            )
            session
        }
    }
}
