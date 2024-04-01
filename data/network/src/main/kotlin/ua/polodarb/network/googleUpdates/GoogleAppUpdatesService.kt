package ua.polodarb.network.googleUpdates

import tw.ktrssreader.kotlin.model.channel.RssStandardChannel

interface GoogleAppUpdatesService {

    suspend fun getLatestRelease(): RssStandardChannel

}