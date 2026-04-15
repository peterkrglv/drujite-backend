package services

import db.repos.NewsRepository
import models.NewsModel

class NewsService(
    private val newsRepository: NewsRepository,
) {
    suspend fun add(news: NewsModel) = newsRepository.add(news)

    suspend fun get(id: Int) = newsRepository.get(id)

    suspend fun delete(id: Int) = newsRepository.delete(id)

    suspend fun getSessionsNews(sessionId: Int) = newsRepository.getSessionsNews(sessionId)
}
