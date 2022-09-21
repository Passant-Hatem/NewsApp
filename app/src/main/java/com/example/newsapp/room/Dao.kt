package com.example.newsapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.newsapp.model.Article

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}