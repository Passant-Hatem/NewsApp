package com.example.newsapp.repo

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.dp.room.ArticleDataBase
import com.example.newsapp.model.Article

class NewsRepo(
    private val db: ArticleDataBase
) {

    suspend fun getNews(countryCode: String, pagesNumber: Int) =
        RetrofitInstance.api.getNews(
            countryCode,
            pagesNumber
        )

    suspend fun searchForNews(searchQuery: String, pagesNumber: Int) =
        RetrofitInstance.api.searchForNews(
            searchQuery,
            pagesNumber
        )

    suspend fun insertArticle(article: Article) = db.getArticleDao().insert(article)

    fun getSavedArticle() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}