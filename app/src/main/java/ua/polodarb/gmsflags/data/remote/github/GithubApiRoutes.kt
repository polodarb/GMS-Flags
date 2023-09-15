package ua.polodarb.gmsflags.data.remote.github

object GithubApiRoutes {
    private const val BASE_URL = "https://api.github.com/repos/polodarb/GMS-Flags"
    const val RELEASE_UPDATES = "$BASE_URL/releases/latest"
}