package com.example.news.core.prensentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.news.article.presenation.ArticleScreenCore
import com.example.news.core.prensentation.ui.theme.NewsAppTheme
import com.example.news.news.prensentation.NewsScreenCore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsAppTheme {
                Navigation()
            }
        }
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.News
        ) {

            composable<Screen.News> {
                NewsScreenCore {
                    navController.navigate(Screen.Article(it))
                }
            }
            composable<Screen.Article> { backStackEntry ->
                val article: Screen.Article = backStackEntry.toRoute()
                ArticleScreenCore(articleId = article.articleId)
            }

        }

    }

}

