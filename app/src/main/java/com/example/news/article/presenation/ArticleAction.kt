package com.example.news.article.presenation

/**
 * @author Ahmed Guedmioui
 */

sealed interface ArticleAction {
    data class LoadArticle(val articleId: String) : ArticleAction
}