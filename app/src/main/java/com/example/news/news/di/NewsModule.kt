package com.example.news.news.di

import com.example.news.news.prensentation.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {
    viewModel { NewsViewModel(get()) }
}