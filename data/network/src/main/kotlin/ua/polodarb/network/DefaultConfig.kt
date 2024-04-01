package ua.polodarb.network

import android.util.Log
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.setConfig(tag: String) {
    install(Logging) { this.setConfig(tag = tag) }
    install(ContentNegotiation) { this.setConfig() }
    install(HttpTimeout) { this.setConfig() }
}

private fun Logging.Config.setConfig(tag: String) {
//    this.level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE // todo
    this.logger = object: Logger {
        override fun log(message: String) {
            Log.e(tag, message)
        }
    }
}

private fun ContentNegotiation.Config.setConfig() {
    this.json(json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    })
}

private const val TIMEOUT = 5000L

private fun HttpTimeout.HttpTimeoutCapabilityConfiguration.setConfig() {
    requestTimeoutMillis = TIMEOUT
    connectTimeoutMillis = TIMEOUT
    socketTimeoutMillis = TIMEOUT
}
