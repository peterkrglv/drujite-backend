package db.repos

import models.ClanModel

interface ClanRepository {
    suspend fun add(clan: ClanModel): Int

    suspend fun get(id: Int): ClanModel?

    suspend fun delete(id: Int): Boolean

    suspend fun addClanToSession(
        clanId: Int,
        sessionId: Int,
    ): Boolean

    suspend fun deleteClanFromSession(
        clanId: Int,
        sessionId: Int,
    ): Boolean

    suspend fun getSessionsClans(sessionId: Int): List<ClanModel>

    suspend fun getAll(): List<ClanModel>
}
