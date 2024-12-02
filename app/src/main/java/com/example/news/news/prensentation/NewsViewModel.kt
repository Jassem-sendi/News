package com.example.news.news.prensentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.core.domain.NewsRepository
import com.example.news.core.domain.NewsResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var state by mutableStateOf(NewsState())
        private set

    init {
        loadNews()
    }

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Paginate -> {
                paginate()
            }


            is NewsAction.OnQueryChange -> {
                state = state.copy(
                    query = action.query
                )
                searchArticle(state.query)

            }
        }
    }

    var searchJob: Job? = null
    private fun searchArticle(query: String) {
        if (query == state.query) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(100)

                state = state.copy(
                    isLoading = true
                )
                newsRepository.searchArticle(query).let { newResult ->
                    state = when (newResult) {
                        is NewsResult.Error -> {
                            state.copy(isError = true)
                        }

                        is NewsResult.Success -> {
                            state.copy(
                                isError = false ,
                                articleList = newResult.data?.articles ?: emptyList() ,
                                nextPage = newResult.data?.nextPage
                            )
                        }
                    }
                }

                state = state.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            newsRepository.getNews().let { newResult ->
                state = when (newResult) {
                    is NewsResult.Error -> {
                        state.copy(isError = true)
                    }

                    is NewsResult.Success -> {
                        state.copy(
                            isError = false ,
                            articleList = newResult.data?.articles ?: emptyList() ,
                            nextPage = newResult.data?.nextPage
                        )
                    }
                }
            }

            state = state.copy(
                isLoading = false
            )
        }
    }

    private fun paginate() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true ,
                isPaginating = true
            )
            newsRepository.paginate(state.nextPage).let { newResult ->

                state = when (newResult) {
                    is NewsResult.Error -> {
                        state.copy(isError = true)
                    }

                    is NewsResult.Success -> {
                        val articles = newResult.data?.articles ?: emptyList()
                        state.copy(
                            isError = false ,
                            articleList = state.articleList + articles ,
                            nextPage = newResult.data?.nextPage ,
                        )
                    }
                }
            }

            state = state.copy(
                isLoading = false ,
                isPaginating = false
            )
        }
    }

}