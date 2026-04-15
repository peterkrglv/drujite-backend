package services

import db.repos.ClanRepository
import models.ClanModel

class ClanService(
    private val clanRepository: ClanRepository,
) {
    suspend fun getClan(clanId: Int) = clanRepository.get(clanId)

    suspend fun addClan(clanModel: ClanModel) = clanRepository.add(clanModel)

    suspend fun deleteClan(clanId: Int) = clanRepository.delete(clanId)

    suspend fun addClanToSession(
        clanId: Int,
        sessionId: Int,
    ) = clanRepository.addClanToSession(clanId, sessionId)

    suspend fun deleteClanFromSession(
        clanId: Int,
        sessionId: Int,
    ) = clanRepository.deleteClanFromSession(clanId, sessionId)

    suspend fun getSessionsClans(sessionId: Int) = clanRepository.getSessionsClans(sessionId)

    suspend fun getAllClans() = clanRepository.getAll()
}
