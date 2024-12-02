package com.example.news.core.domain

interface NewsRepository {

    suspend fun getNews(): NewsResult<NewsList>
    suspend fun paginate(nextPage: String?): NewsResult<NewsList>
    suspend fun getArticle(articleId: String): NewsResult<Article>
    suspend fun searchArticle(query: String): NewsResult<NewsList>
}