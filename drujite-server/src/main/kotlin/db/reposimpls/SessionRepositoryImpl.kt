package db.reposimpls

import db.mapping.SessionDAO
import db.mapping.SessionTable
import db.mapping.UsersSessionsDAO
import db.mapping.UsersSessionsTable
import db.mapping.daoToModel
import db.mapping.suspendTransaction
import db.repos.SessionRepository
import models.SessionModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class SessionRepositoryImpl : SessionRepository {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override suspend fun get(id: Int): SessionModel? =
        suspendTransaction {
            SessionDAO.findById(id)?.let(::daoToModel)
        }

    override suspend fun add(session: SessionModel): Int =
        suspendTransaction {
            val id =
                SessionDAO
                    .new {
                        name = session.name
                        description = session.description
                        startDate = LocalDateTime.parse(session.startDate, formatter)
                        endDate = LocalDateTime.parse(session.endDate, formatter)
                        imageUrl = session.imageUrl
                    }.id.value
            val qrLink = "https://divinaition.online/drujite/session-qr/" + id.toString()
            SessionDAO.findById(id)?.let {
                it.qr = qrLink
                it.flush()
            }
            id
        }

    override suspend fun delete(id: Int): Boolean =
        suspendTransaction {
            SessionDAO.findById(id)?.delete() != null
        }

    override suspend fun getSessionsByUserId(userId: UUID): List<SessionModel> =
        suspendTransaction {
            UsersSessionsDAO
                .find { UsersSessionsTable.userId eq userId }
                .mapNotNull { it.sessionId.let(SessionDAO::findById)?.let(::daoToModel) }
        }

    override suspend fun getAll(): List<SessionModel> =
        suspendTransaction {
            SessionDAO.all().map(::daoToModel)
        }

    override suspend fun addImageUrl(
        sessionId: Int,
        imageUrl: String,
    ): Boolean =
        suspendTransaction {
            SessionDAO.findById(sessionId)?.let {
                it.imageUrl = imageUrl
                it.flush()
                true
            } ?: false
        }

    override suspend fun getSessionByQr(qr: String): SessionModel? =
        suspendTransaction {
            SessionDAO.find { SessionTable.qr eq qr }.firstOrNull()?.let(::daoToModel)
        }
}
