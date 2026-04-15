package models

data class NewsModel(
    val content: String,
    val dateTime: String,
    val id: Int,
    val sessionId: Int,
    val title: String,
    val imageUrl: String? = null,
)
