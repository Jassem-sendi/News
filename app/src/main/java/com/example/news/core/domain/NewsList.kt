package com.example.news.core.domain

import com.example.news.core.domain.Article


data class NewsList(
    val nextPage: String? ,
    val articles: List<Article> ,
)