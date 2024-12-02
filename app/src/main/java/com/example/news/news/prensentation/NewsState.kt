package com.example.news.news.prensentation
import com.example.news.core.domain.Article

data class NewsState(
    val articleList: List<Article> = emptyList() ,
    val nextPage: String? = null ,
    val isLoading: Boolean = false ,
    val isError: Boolean = false ,
    val isPaginating: Boolean = false ,
    val query: String = ""
)
