package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.SerialName

enum class FlagType {
    @SerialName("bool") BOOL,
    @SerialName("int") INTEGER,
    @SerialName("float") FLOAT,
    @SerialName("string") STRING
}
