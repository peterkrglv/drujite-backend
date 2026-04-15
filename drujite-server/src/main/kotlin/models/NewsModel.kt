package models

data class NewsModel(
    val id: Int,
    val sessionId: Int,
    val title: String,
    val content: String,
    val dateTime: String,
    val imageUrl: String? = null,
)
