package db.reposimpls

import db.mapping.UserDAO
import db.mapping.UserTable
import db.mapping.daoToModel
import db.mapping.suspendTransaction
import db.repos.UserRepository
import models.UserModel
import ru.drujite.util.SecurityUtils
import java.util.UUID

class UserRepositoryImpl : UserRepository {
    override suspend fun get(id: UUID): UserModel? =
        suspendTransaction {
            UserDAO.findById(id)?.let { daoToModel(it) }
        }

    override suspend fun getByPhone(phone: String): UserModel? =
        suspendTransaction {
            val encryptedPhone = SecurityUtils.encrypt(phone)
            UserDAO.find { UserTable.phone eq encryptedPhone }.firstOrNull()?.let { daoToModel(it) }
        }

    override suspend fun add(user: UserModel): UserModel? =
        suspendTransaction {
            UserDAO
                .new {
                    username = user.username
                    phone = user.phone
                    password = user.password
                    gender = user.gender
                }.let { daoToModel(it) }
        }

    override suspend fun delete(id: UUID): Boolean =
        suspendTransaction {
            UserDAO.findById(id)?.let {
                it.delete()
                true
            } ?: false
        }

    override suspend fun checkIsAdmin(id: UUID): Boolean =
        suspendTransaction {
            UserDAO.findById(id)?.isAdmin ?: false
        }

    override suspend fun makeAdmin(id: UUID): Boolean =
        suspendTransaction {
            UserDAO.findById(id)?.let {
                it.isAdmin = !it.isAdmin
                true
            } == true
        }

    override suspend fun checkIfSuperAdmin(id: UUID): Boolean =
        suspendTransaction {
            UserDAO.findById(id)?.isSuperAdmin == true
        }

    override suspend fun getAllUsers(): List<UserModel> =
        suspendTransaction {
            UserDAO.all().map { daoToModel(it) }
        }
}
