package com.example.news

import android.app.Application
import com.ag_apps.newsapp.article.di.articleModule
import com.example.news.core.di.coreModule
import com.example.news.news.di.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule ,
                newsModule ,
                articleModule
            )
        }
    }

}