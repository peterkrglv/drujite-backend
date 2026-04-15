package db.reposimpls

import db.mapping.ClanDAO
import db.mapping.SessionClansDAO
import db.mapping.SessionDAO
import db.mapping.SessionsClansTable
import db.mapping.daoToModel
import db.mapping.suspendTransaction
import db.repos.ClanRepository
import models.ClanModel
import org.jetbrains.exposed.sql.and

class ClanRepositoryImpl : ClanRepository {
    override suspend fun add(clan: ClanModel): Int =
        suspendTransaction {
            ClanDAO
                .new {
                    name = clan.name
                    description = clan.description
                }.id.value
        }

    override suspend fun get(id: Int): ClanModel? =
        suspendTransaction {
            val clan = ClanDAO.findById(id)
            clan?.let { daoToModel(it) }
        }

    override suspend fun delete(id: Int): Boolean =
        suspendTransaction {
            (ClanDAO.findById(id))?.delete() != null
        }

    override suspend fun addClanToSession(
        clanId: Int,
        sessionId: Int,
    ): Boolean =
        suspendTransaction {
            val clan = ClanDAO.findById(clanId)
            val session = SessionDAO.findById(sessionId)
            if (clan != null && session != null) {
                SessionClansDAO
                    .new {
                        this.clanId = clanId
                        this.sessionId = sessionId
                    }.id.value > 0
            } else {
                false
            }
        }

    override suspend fun deleteClanFromSession(
        clanId: Int,
        sessionId: Int,
    ): Boolean =
        suspendTransaction {
            val sessionClan =
                SessionClansDAO
                    .find {
                        (SessionsClansTable.clanId eq clanId) and (SessionsClansTable.sessionId eq sessionId)
                    }.firstOrNull()
            if (sessionClan != null) {
                sessionClan.delete()
                true
            } else {
                false
            }
        }

    override suspend fun getSessionsClans(sessionId: Int): List<ClanModel> =
        suspendTransaction {
            SessionClansDAO
                .find { SessionsClansTable.sessionId eq sessionId }
                .mapNotNull { sessionClan ->
                    ClanDAO.findById(sessionClan.clanId)?.let { daoToModel(it) }
                }
        }

    override suspend fun getAll(): List<ClanModel> =
        suspendTransaction {
            ClanDAO.all().map { daoToModel(it) }
        }
}
