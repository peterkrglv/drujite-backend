package db.reposimpls

import db.mapping.SessionDAO
import db.mapping.UserDAO
import db.mapping.UserTable
import db.mapping.UsersSessionsDAO
import db.mapping.UsersSessionsTable
import db.mapping.daoToModel
import db.mapping.suspendTransaction
import db.repos.UsersSessionsRepository
import models.SessionModel
import models.UserModel
import org.jetbrains.exposed.sql.and
import java.util.UUID

class UsersSessionsRepositoryImpl : UsersSessionsRepository {
    override suspend fun addUserSession(
        userId: UUID,
        sessionId: Int,
        characterId: Int?,
    ): Boolean {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find { UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) }
                    .firstOrNull()
            if (existingSession != null) {
                if (characterId != null) {
                    existingSession.characterId = characterId
                    return@suspendTransaction true
                } else {
                    return@suspendTransaction false
                }
            } else {
                UsersSessionsDAO.new {
                    this.userId = userId
                    this.sessionId = sessionId
                    this.characterId = characterId
                }
                return@suspendTransaction true
            }
        }
    }

    override suspend fun addCharacter(
        userId: UUID,
        sessionId: Int,
        characterId: Int,
        transferReason: String?,
    ): Boolean {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find { UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) }
                    .firstOrNull()
            if (existingSession != null) {
                existingSession.characterId = characterId
                existingSession.transferReason = transferReason
                return@suspendTransaction true
            } else {
                return@suspendTransaction false
            }
        }
    }

    override suspend fun deleteUserSession(
        userId: UUID,
        sessionId: Int,
    ): Boolean {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find { UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) }
                    .firstOrNull()
            if (existingSession != null) {
                existingSession.delete()
                return@suspendTransaction true
            } else {
                return@suspendTransaction false
            }
        }
    }

    override suspend fun deleteCharacter(
        userId: UUID,
        sessionId: Int,
        characterId: Int,
    ): Boolean {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find {
                        UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) and
                            (UsersSessionsTable.characterId eq characterId)
                    }.firstOrNull()
            if (existingSession != null) {
                existingSession.characterId = null
                return@suspendTransaction true
            } else {
                return@suspendTransaction false
            }
        }
    }

    override suspend fun getCharacterId(
        userId: UUID,
        sessionId: Int,
    ): Int? {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find { UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) }
                    .firstOrNull()
            return@suspendTransaction existingSession?.characterId
        }
    }

    override suspend fun getCharacterIds(userId: UUID): List<Int> =
        suspendTransaction {
            UsersSessionsDAO
                .find { UsersSessionsTable.userId eq userId }
                .mapNotNull { it.characterId }
                .distinct()
        }

    override suspend fun getSessionsCharactersIds(sessionId: Int): List<Int> =
        suspendTransaction {
            UsersSessionsDAO
                .find { UsersSessionsTable.sessionId eq sessionId }
                .mapNotNull { it.characterId }
        }

    override suspend fun getUsersSessions(userId: UUID): List<SessionModel> =
        suspendTransaction {
            UsersSessionsDAO
                .find { UsersSessionsTable.userId eq userId }
                .mapNotNull { it.sessionId.let(SessionDAO::findById)?.let(::daoToModel) }
        }

    override suspend fun getUserByCharacter(characterId: Int): UserModel? =
        suspendTransaction {
            val userId =
                UsersSessionsDAO
                    .find { UsersSessionsTable.characterId eq characterId }
                    .firstNotNullOfOrNull { it.userId }
            val user =
                userId?.let {
                    UserDAO.find { UserTable.id eq it }.firstOrNull()
                }
            user?.let { daoToModel(it) }
        }

    override suspend fun getIdByCharacterAndSession(
        characterId: Int,
        sessionId: Int,
    ): Int? {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find {
                        UsersSessionsTable.characterId eq characterId and
                            (UsersSessionsTable.sessionId eq sessionId)
                    }.firstOrNull()
            return@suspendTransaction existingSession?.id?.value
        }
    }

    override suspend fun getIdByUserAndSession(
        userId: UUID,
        sessionId: Int,
    ): Int? {
        return suspendTransaction {
            val existingSession =
                UsersSessionsDAO
                    .find { UsersSessionsTable.userId eq userId and (UsersSessionsTable.sessionId eq sessionId) }
                    .firstOrNull()
            return@suspendTransaction existingSession?.id?.value
        }
    }
}
