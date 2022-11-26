package com.example.byeprivacy.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import kotlin.experimental.and


fun generateSalt(): ByteArray {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)
    return salt
}

fun hashPassword(password: String): String {
    var generatedPassword: String? = null
    try {
        val md = MessageDigest.getInstance("SHA-512")
        val bytes = md.digest(password.toByteArray())
        val sb = StringBuilder()
        for (i in bytes.indices) {
            sb.append(
                Integer.toString((bytes[i] and 0xff.toByte()) + 0x100, 16)
                    .substring(1)
            )
        }
        generatedPassword = sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return generatedPassword!!
}