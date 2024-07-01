package ua.polodarb.byteUtils

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

    fun textAsBytes(hexString: String): ByteArray {
        val cleanedHexString = hexString.replace("0x", "").replace("\\s".toRegex(), "")
        require(cleanedHexString.length % 2 == 0) { "Invalid hex string length" }

        return try {
            ByteArray(cleanedHexString.length / 2) {
                cleanedHexString.substring(it * 2, it * 2 + 2).toInt(16).toByte()
            }
        } catch (e: NumberFormatException) {
            throw ByteUtilsException("Invalid hex string format", e)
        }
    }
}

class ByteUtilsException(message: String, cause: Throwable? = null) : Exception(message, cause)