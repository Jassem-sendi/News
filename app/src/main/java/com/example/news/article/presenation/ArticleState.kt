package com.example.news.article.presenation

import com.example.news.core.domain.Article

/**
 * @author Ahmed Guedmioui
 */
data class ArticleState(
    val article: Article? = null ,
    val isLoading: Boolean = false ,
    val isError: Boolean = false
)
