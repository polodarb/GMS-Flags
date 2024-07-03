package ua.polodarb.byteUtils

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class ByteUtils {

    fun textToBytes(text: String): ByteArray {
        return try {
            text.toByteArray().flatMap {
                listOf((it.toInt() shr 4 and 0xF).toByte(), (it.toInt() and 0xF).toByte())
            }.toByteArray()
        } catch (e: Exception) {
            throw ByteUtilsException("Error converting text to bytes", e)
        }
    }

    fun convertToByteArray(hexText: String?): ByteArray? {
        if (hexText.isNullOrEmpty()) return null
        return ByteArrayOutputStream().use { bytes ->
            ObjectOutputStream(bytes).writeObject(hexText)
            bytes.toByteArray()
        }
    }
}

class ByteUtilsException(message: String, cause: Throwable? = null) : Exception(message, cause)