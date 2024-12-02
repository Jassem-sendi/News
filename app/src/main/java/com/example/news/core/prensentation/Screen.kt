package com.example.news.core.prensentation


sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object News : Screen

    @kotlinx.serialization.Serializable
    data class Article(val articleId: String) : Screen

}