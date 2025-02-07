package com.ag_apps.newsapp.article.di

import com.example.news.article.presenation.ArticleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val articleModule = module {
    viewModel { ArticleViewModel(get()) }
}