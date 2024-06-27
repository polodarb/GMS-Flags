package ua.polodarb.protobuf

import com.google.protobuf.ByteString
import com.google.protobuf.MessageLite
import java.io.IOException

class ProtobufManager {

    inline fun <reified T : MessageLite> deserialize(bytes: ByteArray): Result<T> {
        return try {
            val method = T::class.java.getMethod("parseFrom", ByteArray::class.java)
            val result = method.invoke(null, bytes) as T
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(ProtobufException("Failed to deserialize protobuf message", e))
        }
    }

    fun serialize(message: MessageLite): Result<ByteArray> {
        return try {
            val bytes = message.toByteArray()
            Result.success(bytes)
        } catch (e: IOException) {
            Result.failure(ProtobufException("Failed to serialize protobuf message", e))
        }
    }

    fun toByteString(message: MessageLite): Result<ByteString> {
        return try {
            val byteString = message.toByteString()
            Result.success(byteString)
        } catch (e: Exception) {
            Result.failure(ProtobufException("Failed to convert protobuf message to ByteString", e))
        }
    }

    inline fun <reified T : MessageLite> fromByteString(byteString: ByteString): Result<T> {
        return try {
            val method = T::class.java.getMethod("parseFrom", ByteString::class.java)
            val result = method.invoke(null, byteString) as T
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(ProtobufException("Failed to parse ByteString to protobuf message", e))
        }
    }
}

class ProtobufException(message: String, cause: Throwable? = null) : Exception(message, cause)