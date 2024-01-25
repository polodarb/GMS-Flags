package ua.polodarb.gmsflags.data.remote.googleUpdates

import tw.ktrssreader.kotlin.model.channel.RssStandardChannel

interface GoogleAppUpdatesService {

    suspend fun getLatestRelease(): RssStandardChannel

}