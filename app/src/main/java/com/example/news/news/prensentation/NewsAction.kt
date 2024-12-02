package com.example.news.news.prensentation


sealed interface NewsAction {
    data object Paginate: NewsAction
    data class OnQueryChange(val query: String): NewsAction

}