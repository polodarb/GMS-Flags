package ua.polodarb.gmsflags.data.repo.mappers

import tw.ktrssreader.kotlin.model.channel.RssStandardChannel
import tw.ktrssreader.kotlin.model.channel.RssStandardChannelData
import tw.ktrssreader.kotlin.model.item.RssStandardItem
import ua.polodarb.gmsflags.data.remote.googleUpdates.dto.Article
import ua.polodarb.gmsflags.data.remote.googleUpdates.dto.RssMainModel

class GoogleUpdatesMapper {

    fun map(response: RssStandardChannel): NewRssModel {
        return NewRssModel(
            articles = mapArticle(response.items.orEmpty())
        )
    }

    private fun mapArticle(response: List<RssStandardItem>): List<NewRssArticle> {
        return response.mapNotNull { article ->
            val regex = Regex("""(.+?)\s(\d+\.\d+\.\d+)""")
            val matchResult = regex.find(article.title.orEmpty())

            matchResult?.let {
                val appName = it.groupValues[1]
                val appVersion = it.groupValues[2]

                NewRssArticle(
                    title = appName,
                    version = appVersion,
                    date = article.pubDate.orEmpty(),
                    link = article.link.orEmpty()
                )
            }
        }.filter {
            !(it.title.contains("Wear OS", ignoreCase = true) ||
                    it.title.contains("Android TV", ignoreCase = true) ||
                    it.title.contains("Trichrome", ignoreCase = true))


        }
    }

}

data class NewRssModel(
    val articles: List<NewRssArticle>
)

data class NewRssArticle(
    val title: String,
    val version: String,
    val date: String,
    val link: String,
)