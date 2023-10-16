package ua.polodarb.gmsflags.data.remote.github

import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.github.dto.ReleaseInfo

interface GithubApiService {
    suspend fun getLatestRelease(): Resource<ReleaseInfo>
}
