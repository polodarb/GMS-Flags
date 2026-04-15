package ua.polodarb.gms.impl

import com.google.protobuf.CodedInputStream
import com.google.protobuf.CodedOutputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

internal object PhixitFlagsCodec {
    fun decode(compressedData: ByteArray): List<PhixitFlag> {
        val input = CodedInputStream.newInstance(decompress(compressedData))
        val size = input.readUInt32()
        var next = 0L

        return List(size) {
            val theory = input.readUInt64()
            val shift = theory ushr 3
            val name = if (shift == 0L) {
                input.readString()
            } else {
                (shift + next).also { next = it }.toString()
            }

            when (val type = theory.toInt() and 7) {
                0, 1 -> PhixitFlag.Bool(name, type != 0)
                2 -> PhixitFlag.Int(name, input.readUInt64())
                3 -> PhixitFlag.Float(name, java.lang.Double.doubleToRawLongBits(input.readDouble()))
                4 -> PhixitFlag.StringValue(name, input.readString())
                5 -> PhixitFlag.Extension(name, input.readByteArray())
                else -> throw IllegalArgumentException("Unknown flag type: $type")
            }
        }
    }

    fun encode(flags: List<PhixitFlag>): ByteArray {
        val out = ByteArrayOutputStream()
        val cos = CodedOutputStream.newInstance(out)
        var next = 0L

        cos.writeUInt32NoTag(flags.size)
        flags.forEach { flag ->
            val nameAsLong = flag.name.toLongOrNull()
            if (nameAsLong != null) {
                cos.writeUInt64NoTag(((nameAsLong - next) shl 3) or flag.type)
                next = nameAsLong
            } else {
                cos.writeUInt64NoTag(flag.type)
                cos.writeStringNoTag(flag.name)
            }

            when (flag) {
                is PhixitFlag.Bool -> Unit
                is PhixitFlag.Int -> cos.writeUInt64NoTag(flag.value)
                is PhixitFlag.Float -> cos.writeDoubleNoTag(java.lang.Double.longBitsToDouble(flag.value))
                is PhixitFlag.StringValue -> cos.writeStringNoTag(flag.value)
                is PhixitFlag.Extension -> cos.writeByteArrayNoTag(flag.value)
            }
        }

        cos.flush()
        return compress(out.toByteArray())
    }

    private fun decompress(compressedData: ByteArray): ByteArray {
        val inflater = Inflater(true).apply { setInput(compressedData) }
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        while (!inflater.finished()) {
            val count = inflater.inflate(buffer)
            if (count == 0 && inflater.needsInput()) break
            out.write(buffer, 0, count)
        }
        inflater.end()
        return out.toByteArray()
    }

    private fun compress(data: ByteArray): ByteArray {
        val deflater = Deflater(1, true).apply {
            setInput(data)
            finish()
        }
        val out = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        while (!deflater.finished()) {
            val count = deflater.deflate(buffer)
            out.write(buffer, 0, count)
        }
        deflater.end()
        return out.toByteArray()
    }
}

internal sealed class PhixitFlag {
    abstract val name: String
    abstract val type: Long

    data class Bool(override val name: String, val value: Boolean) : PhixitFlag() {
        override val type: Long = if (value) 1 else 0
    }

    data class Int(override val name: String, val value: Long) : PhixitFlag() {
        override val type: Long = 2
    }

    data class Float(override val name: String, val value: Long) : PhixitFlag() {
        override val type: Long = 3
    }

    data class StringValue(override val name: String, val value: String) : PhixitFlag() {
        override val type: Long = 4
    }

    data class Extension(override val name: String, val value: ByteArray) : PhixitFlag() {
        override val type: Long = 5
    }
}
