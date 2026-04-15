package db.mapping

import models.UserModel
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import ru.drujite.util.SecurityUtils
import java.util.UUID

object UserTable : UUIDTable("users") {
    val phone = varchar("phone", DbStringLength.USER_LOGIN_FIELD).uniqueIndex()
    val username = varchar("username", DbStringLength.USER_LOGIN_FIELD)
    val password = varchar("password", DbStringLength.PASSWORD_HASH)
    val gender = varchar("gender", DbStringLength.GENDER)
    val isAdmin = bool("is_admin").default(false)
    val isSuperAdmin = bool("is_superadmin").default(false)
}

class UserDAO(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(UserTable)

    var phone by UserTable.phone.transform(
        { value ->
            println("Encrypting phone (to database): $value")
            SecurityUtils.encrypt(value)
        },
        { value ->
            println("Decrypting phone (from database): $value")
            SecurityUtils.decrypt(value)
        },
    )
    var username by UserTable.username.transform(
        { SecurityUtils.encrypt(it) },
        { SecurityUtils.decrypt(it) },
    )
    var password by UserTable.password.transform(
        { SecurityUtils.hashPassword(it) },
        { it },
    )
    var gender by UserTable.gender
    var isAdmin by UserTable.isAdmin
    var isSuperAdmin by UserTable.isSuperAdmin
}

fun daoToModel(dao: UserDAO) =
    UserModel(
        gender = dao.gender,
        id = dao.id.value,
        password = dao.password,
        phone = dao.phone,
        username = dao.username,
        isAdmin = dao.isAdmin,
    )
