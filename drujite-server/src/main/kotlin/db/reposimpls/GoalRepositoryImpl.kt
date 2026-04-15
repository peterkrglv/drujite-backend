package db.reposimpls

import db.mapping.GoalDAO
import db.mapping.GoalTable
import db.mapping.UsersSessionsTable
import db.mapping.suspendTransaction
import db.repos.GoalRepository
import models.GoalModel
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.drujite.models.GoalModelWithCharacterdId
import java.sql.SQLException

class GoalRepositoryImpl : GoalRepository {
    private val logger: Logger = LoggerFactory.getLogger(GoalRepositoryImpl::class.java)

    override suspend fun add(goal: GoalModel): Int =
        suspendTransaction {
            GoalDAO
                .new {
                    usersSessionId = goal.usersSessionId
                    name = goal.name
                    isCompleted = goal.isCompleted
                }.id.value
        }

    override suspend fun get(id: Int): GoalModel? =
        suspendTransaction {
            GoalDAO.findById(id)?.let {
                GoalModel(
                    id = it.id.value,
                    usersSessionId = it.usersSessionId,
                    name = it.name,
                    isCompleted = it.isCompleted,
                )
            }
        }

    override suspend fun delete(id: Int): Boolean =
        suspendTransaction {
            GoalDAO.findById(id)?.let {
                it.delete()
                true
            } ?: false
        }

    override suspend fun getCharacterGoals(usersSessionId: Int): List<GoalModel> =
        suspendTransaction {
            GoalDAO.find { GoalTable.usersSessionId eq usersSessionId }.map {
                GoalModel(
                    id = it.id.value,
                    usersSessionId = it.usersSessionId,
                    name = it.name,
                    isCompleted = it.isCompleted,
                )
            }
        }

    override suspend fun changeStatus(id: Int): Boolean =
        suspendTransaction {
            GoalDAO.findById(id)?.let {
                it.isCompleted = !it.isCompleted
                it.flush()
                true
            } ?: false
        }

    override suspend fun getSessionsGoals(sessionId: Int): List<GoalModelWithCharacterdId> =
        suspendTransaction {
            try {
                logger.info("Starting getSessionsGoals with sessionId: $sessionId")
                val results =
                    GoalTable
                        .innerJoin(
                            UsersSessionsTable,
                            { usersSessionId },
                            { id },
                            additionalConstraint = { UsersSessionsTable.sessionId eq sessionId },
                        ).selectAll()
                        .mapNotNull {
                            val characterId = it[UsersSessionsTable.characterId]
                            if (characterId == null) {
                                logger.warn("Character ID is null for sessionId: $sessionId")
                                null
                            } else {
                                GoalModelWithCharacterdId(
                                    id = it[GoalTable.id].value,
                                    characterId = it[UsersSessionsTable.characterId] ?: -1,
                                    name = it[GoalTable.name],
                                    isCompleted = it[GoalTable.isCompleted],
                                )
                            }
                        }
                logger.info("Finished processing rows. Total goals: ${results.size}")
                results
            } catch (e: SQLException) {
                logger.error("Error in getSessionsGoals: ${e.message}", e)
                emptyList()
            }
        }
}
