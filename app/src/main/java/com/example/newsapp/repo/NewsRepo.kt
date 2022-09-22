package com.example.newsapp.repo

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.dp.room.ArticleDataBase

class NewsRepo (
    val db: ArticleDataBase
    ) {

    suspend fun getNews(countryCode: String, pagesNumber: Int) =
        RetrofitInstance.api.getNews(
            countryCode,
            pagesNumber
        )

    suspend fun searchForNews(searchQuery: String ,pagesNumber: Int) =
        RetrofitInstance.api.searchForNews(
            searchQuery,
            pagesNumber
        )
}