package ua.polodarb.gmsflags.data.remote.github

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import ua.polodarb.gmsflags.BuildConfig
import java.net.UnknownHostException

class GithubApiServiceImpl(
    private val client: HttpClient
): GithubApiService {
    override suspend fun getLatestRelease(): GithubUpdateModel {
        return try {
            client.get { url(GithubApiRoutes.RELEASE_UPDATES) }.body<GithubUpdateModel>()
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            GithubUpdateModel(tagName = BuildConfig.VERSION_NAME)
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            GithubUpdateModel(tagName = BuildConfig.VERSION_NAME)
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            GithubUpdateModel(tagName = BuildConfig.VERSION_NAME)
        } catch (ex: UnknownHostException) {
            println("Offline mode")
            GithubUpdateModel(tagName = BuildConfig.VERSION_NAME)
        }
    }
}