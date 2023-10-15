package ua.polodarb.gmsflags.data.remote.github

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.url
import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.github.dto.Release
import ua.polodarb.gmsflags.data.remote.setConfig

private const val BASE_URL = "https://api.github.com"

class GithubApiServiceImpl(
    engine: HttpClientEngine
): GithubApiService {
    private val client = HttpClient(engine) {
        this.setConfig("GithubApiService")
        defaultRequest {
            url(BASE_URL)
        }
    }

    override suspend fun getLatestRelease(): Resource<Release> {
        return try {
            Resource.Success(client.get {
                url("repos/polodarb/GMS-Flags/releases/latest")
            }.body())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
