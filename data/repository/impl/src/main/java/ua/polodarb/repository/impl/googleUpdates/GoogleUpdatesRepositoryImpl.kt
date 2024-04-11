package ua.polodarb.repository.impl.googleUpdates

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.polodarb.network.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.repository.googleUpdates.GoogleUpdatesRepository
import ua.polodarb.repository.googleUpdates.mapper.GoogleUpdatesMapper
import ua.polodarb.repository.googleUpdates.model.MainRssModel

class GoogleUpdatesRepositoryImpl(
    private val network: GoogleAppUpdatesService,
    private val mapper: GoogleUpdatesMapper
): GoogleUpdatesRepository {

    override suspend fun getLatestRelease(): MainRssModel {
        return withContext(Dispatchers.IO) {
            mapper.map(network.getLatestRelease())
        }
    }

}