package ua.polodarb.network.googleUpdates.model

data class NetworkRssModel(
    val articles: List<NetworkArticle>
)

data class NetworkArticle(
    val title: String,
    val link: String,
    val pubDate: String,
)