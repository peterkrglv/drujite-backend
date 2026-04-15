package db.repos

import models.GoalModel
import ru.drujite.models.GoalModelWithCharacterdId

interface GoalRepository {
    suspend fun add(goal: GoalModel): Int

    suspend fun get(id: Int): GoalModel?

    suspend fun delete(id: Int): Boolean

    suspend fun getCharacterGoals(usersSessionId: Int): List<GoalModel>

    suspend fun changeStatus(id: Int): Boolean

    suspend fun getSessionsGoals(sessionId: Int): List<GoalModelWithCharacterdId>
}
