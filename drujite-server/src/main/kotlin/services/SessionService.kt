package services

import db.repos.SessionRepository
import models.SessionModel
import requests.SessionRequest
import java.util.UUID

class SessionService(
    private val sessionRepository: SessionRepository,
) {
    suspend fun getSession(sessionId: Int) = sessionRepository.get(sessionId)

    suspend fun addSession(request: SessionRequest) =
        sessionRepository.add(
            SessionModel(
                description = request.description,
                endDate = request.endDate,
                id = 0,
                imageUrl = request.imageUrl,
                name = request.name,
                startDate = request.startDate,
            ),
        )

    suspend fun deleteSession(id: Int) = sessionRepository.delete(id)

    suspend fun getSessionsByUserId(userId: String) =
        sessionRepository.getSessionsByUserId(
            UUID.fromString(userId),
        )

    suspend fun getAllSessions() = sessionRepository.getAll()
}
