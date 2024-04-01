package ua.polodarb.repository.googleUpdates

import ua.polodarb.repository.googleUpdates.model.MainRssModel

interface GoogleUpdatesRepository {

    suspend fun getLatestRelease(): MainRssModel

}