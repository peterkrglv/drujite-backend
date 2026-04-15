package db.repos

import models.UserModel
import java.util.UUID

interface UserRepository {
    suspend fun get(id: UUID): UserModel?

    suspend fun getByPhone(phone: String): UserModel?

    suspend fun add(user: UserModel): UserModel?

    suspend fun delete(id: UUID): Boolean

    suspend fun checkIsAdmin(id: UUID): Boolean

    suspend fun makeAdmin(id: UUID): Boolean

    suspend fun checkIfSuperAdmin(id: UUID): Boolean

    suspend fun getAllUsers(): List<UserModel>
}
