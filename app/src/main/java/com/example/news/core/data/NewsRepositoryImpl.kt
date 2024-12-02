package com.example.news.core.data

import com.example.news.core.data.local.ArticlesDao
import com.ag_apps.newsapp.core.data.remote.NewsListDto
import com.example.news.core.domain.Article
import com.example.news.core.domain.NewsList
import com.example.news.core.domain.NewsRepository
import com.example.news.core.domain.NewsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.coroutines.cancellation.CancellationException


class NewsRepositoryImpl(
    private val httpClient: HttpClient ,
    private val dao: ArticlesDao
) : NewsRepository {

    private val tag = "NewsRepository: "

    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList()
        println(tag + "getLocalNews: " + localNews.size + " nextPage: " + nextPage)
        val newsList = NewsList(
            nextPage = nextPage ,
            articles = localNews.map { it.toArticle() }
        )
        return newsList
    }

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apikey" , apiKey)
            parameter("language" , "en")
            if (nextPage != null) parameter("page" , nextPage)
        }.body()

        println(tag + "getRemoteNews: " + newsListDto.results?.size + " nextPage: " + nextPage)

        return newsListDto.toNewsList()
    }

    override suspend fun getNews(): NewsResult<NewsList> {
        val remoteNewsList = try {
            getRemoteNews(null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "getNews remote exception: " + e.message)
            null
        }

        remoteNewsList?.let {
            dao.clearDatabase()
            dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
            return NewsResult.Success(getLocalNews(remoteNewsList.nextPage))
        }

        val localNewsList = getLocalNews(null)
        if (localNewsList.articles.isNotEmpty()) {
            return NewsResult.Success(localNewsList)
        }

        return NewsResult.Error()
    }

    override suspend fun paginate(nextPage: String?): NewsResult<NewsList> {
        val remoteNewsList = try {
            getRemoteNews(nextPage)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "paginate remote exception: " + e.message)
            null
        }

        remoteNewsList?.let {
            dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
            return NewsResult.Success(remoteNewsList)
        }

        return NewsResult.Error()
    }


    override suspend fun getArticle(
        articleId: String
    ): NewsResult<Article> {
        dao.getArticle(articleId)?.let { localArticle ->
            println(tag + "getArticle local " + localArticle.articleId)
            return NewsResult.Success(localArticle.toArticle())
        }
        try {
            val response: NewsListDto = httpClient.get(baseUrl) {
                parameter("apikey" , apiKey)
                parameter("id" , articleId)
            }.body()
            println(tag + "getArticle remote " + response.results?.size)
            return if (response.results?.isNotEmpty() == true) {
                NewsResult.Success(response.results[0].toArticle())
            } else {
                NewsResult.Error()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(tag + e.message)
            return NewsResult.Error()
        }
    }

    override suspend fun searchArticle(query: String): NewsResult<NewsList> {
        try {
            val response: NewsListDto = httpClient.get(baseUrl) {
                parameter("apikey" , apiKey)
                parameter("q" , query)
            }.body()
            println(tag + "SearchArticle remote " + response.results?.size + " query: " + query)
            return NewsResult.Success(response.toNewsList())
        } catch (e: Exception) {
            e.printStackTrace()
            println(tag + e.message)
            return NewsResult.Error()
        }
    }


    private val baseUrl = "https://newsdata.io/api/1/latest"
    private val apiKey = "pub_6069525c0c583b418e64425c5e4ae428a4fe7"
}