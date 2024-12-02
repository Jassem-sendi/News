package com.example.news.article.presenation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.core.domain.NewsRepository
import com.example.news.core.domain.NewsResult
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val newsRepository: NewsRepository ,
) : ViewModel() {


    var state by mutableStateOf(ArticleState())
        private set

    fun onAction(action: ArticleAction) {
        when (action) {
            is ArticleAction.LoadArticle -> getArticle(action.articleId)
        }
    }

    private fun getArticle(articleId: String) {

        if (articleId.isBlank()) {
            state = state.copy(
               isError = true
            )
            return
        }

        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

           val newsResult=  newsRepository.getArticle(articleId)
                state = when (newsResult) {
                    is NewsResult.Error -> {
                        state.copy(
                            isError = true
                        )
                    }
                    is NewsResult.Success -> {

                        state.copy(
                            article = newsResult.data,
                            isError = false
                        )
                    }
                }
            }

            state = state.copy(
                isLoading = false
            )
        }
    }

