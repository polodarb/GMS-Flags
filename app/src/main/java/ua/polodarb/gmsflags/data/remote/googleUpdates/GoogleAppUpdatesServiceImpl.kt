package ua.polodarb.gmsflags.data.remote.googleUpdates

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.parser.RssStandardParser
import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.setConfig

private const val BASE_URL = "https://www.apkmirror.com/apk/google-inc/feed/"
private const val LOG_TAG = "GoogleUpdatesApiService"

class GoogleAppUpdatesServiceImpl(
    engine: HttpClientEngine
): GoogleAppUpdatesService {

    private val client = HttpClient(engine) {
        this.setConfig(LOG_TAG)
        defaultRequest {
            url(BASE_URL)
        }
    }

    override suspend fun getLatestRelease(): RssStandardChannel {
        val response = client.get(BASE_URL).bodyAsText()
        return RssStandardParser().parse(response)
    }

}