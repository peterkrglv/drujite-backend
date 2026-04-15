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
                id = 0,
                name = request.name,
                description = request.description,
                startDate = request.startDate,
                endDate = request.endDate,
                imageUrl = request.imageUrl,
            ),
        )

    suspend fun deleteSession(id: Int) = sessionRepository.delete(id)

    suspend fun getSessionsByUserId(userId: String) =
        sessionRepository.getSessionsByUserId(
            UUID.fromString(userId),
        )

    suspend fun getAllSessions() = sessionRepository.getAll()
}
