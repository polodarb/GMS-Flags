package ua.polodarb.network.impl.appUpdates

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.url
import ua.polodarb.network.appUpdates.AppUpdatesApiService
import ua.polodarb.network.appUpdates.model.ReleaseInfo
import ua.polodarb.network.Resource
import ua.polodarb.network.setConfig

private const val BASE_URL = "https://api.github.com"
const val LOG_TAG = "GithubApiService"

class AppUpdatesApiServiceImpl(
    engine: HttpClientEngine
): AppUpdatesApiService {
    private val client = HttpClient(engine) {
        this.setConfig(LOG_TAG)
        defaultRequest {
            url(BASE_URL)
        }
    }

    override suspend fun getLatestRelease(): Resource<ReleaseInfo> {
        return try {
            Resource.Success(client.get {
                url("repos/polodarb/GMS-Flags/releases/latest")
            }.body())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
