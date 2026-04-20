package ua.polodarb.common

import java.io.ByteArrayOutputStream

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
