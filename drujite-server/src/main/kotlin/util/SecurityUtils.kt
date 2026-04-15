package ru.drujite.util

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.Key
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {
    private const val AES_ALGORITHM = "AES"
    private const val HEX_BYTE_WIDTH = 2
    private const val HEX_RADIX = 16
    private val secretKey: Key = getKeyFromEnv()

    private fun getKeyFromEnv(): Key {
        val keyBase64 = System.getenv("AES_SECRET_KEY")
        check(!keyBase64.isNullOrBlank()) { "AES_SECRET_KEY is not set in environment variables" }
        val keyBytes = Base64.getDecoder().decode(keyBase64)
        return SecretKeySpec(keyBytes, AES_ALGORITHM)
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val bytes = data.chunked(HEX_BYTE_WIDTH).map { it.toInt(HEX_RADIX).toByte() }.toByteArray()
        return String(cipher.doFinal(bytes))
    }

    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    private val logger: Logger = LoggerFactory.getLogger(SecurityUtils::class.java)

    fun verifyPassword(
        password: String,
        hashed: String,
    ): Boolean {
        val result = BCrypt.checkpw(password, hashed)
        logger.info("Verifying password: $password, hashed: $hashed, result: $result")
        return result
    }
}
