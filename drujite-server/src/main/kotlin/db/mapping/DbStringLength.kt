package db.mapping

/**
 * Shared VARCHAR lengths for Exposed [org.jetbrains.exposed.sql.Table] definitions.
 * Keep aligned with SQL migrations when changing values.
 */
object DbStringLength {
    /** Titles, character names, event names, image URLs, QR payload storage, etc. */
    const val STANDARD = 255

    /** Phone and username columns (stored encrypted; ciphertext needs headroom). */
    const val USER_LOGIN_FIELD = 512

    /** Password hash / bcrypt output. */
    const val PASSWORD_HASH = 255

    /** Short fixed labels (e.g. gender). */
    const val GENDER = 15
}
