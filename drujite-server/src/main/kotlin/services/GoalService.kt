package services

import db.repos.GoalRepository
import db.repos.UsersSessionsRepository
import models.GoalModel
import requests.AddGoalRequest
import ru.drujite.models.GoalModelWithCharacterdId
import java.util.UUID

class GoalService(
    private val goalRepository: GoalRepository,
    private val usersSessionsRepository: UsersSessionsRepository,
) {
    suspend fun getGoal(id: Int) = goalRepository.get(id)

    suspend fun addGoal(goalRequest: AddGoalRequest): Int? {
        val usersSessionId =
            usersSessionsRepository.getIdByCharacterAndSession(goalRequest.characterId, goalRequest.sessionId)
                ?: return null
        val goalModel =
            GoalModel(
                id = 0,
                isCompleted = goalRequest.isCompleted,
                name = goalRequest.name,
                usersSessionId = usersSessionId,
            )
        return goalRepository.add(goalModel)
    }

    suspend fun deleteGoal(id: Int) = goalRepository.delete(id)

    suspend fun getCharacterGoals(
        userId: String,
        sessionId: Int,
    ): List<GoalModel> {
        val usersSessionId =
            usersSessionsRepository.getIdByUserAndSession(UUID.fromString(userId), sessionId)
                ?: return emptyList()
        return goalRepository.getCharacterGoals(usersSessionId)
    }

    suspend fun updateGoalStatus(id: Int) = goalRepository.changeStatus(id)

    suspend fun getSessionsGoals(sessionId: Int): List<GoalModelWithCharacterdId> = goalRepository.getSessionsGoals(sessionId)
}
