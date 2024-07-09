package ua.polodarb.byteUtils

import java.io.ByteArrayOutputStream

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

    fun convertToByteArray(hexString: String?): ByteArray? {
        if (hexString.isNullOrEmpty()) return null
        return ByteArrayOutputStream().use { bytes ->
            for (i in hexString.indices step 2) {
                val byte = hexString.substring(i, i + 2).toInt(16)
                bytes.write(byte)
            }
            bytes.toByteArray()
        }
    }
}

class ByteUtilsException(message: String, cause: Throwable? = null) : Exception(message, cause)