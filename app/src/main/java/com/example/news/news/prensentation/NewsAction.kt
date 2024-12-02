package com.example.news.news.prensentation


sealed interface NewsAction {
    data object Paginate: NewsAction
}