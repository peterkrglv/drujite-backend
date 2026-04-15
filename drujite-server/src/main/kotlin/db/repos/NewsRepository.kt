package db.repos

import models.NewsModel

interface NewsRepository {
    suspend fun add(news: NewsModel): Int

    suspend fun get(id: Int): NewsModel?

    suspend fun delete(id: Int): Boolean

    suspend fun getSessionsNews(sessionId: Int): List<NewsModel>

    suspend fun addImageUrl(
        id: Int,
        imageUrl: String,
    ): Boolean
}
