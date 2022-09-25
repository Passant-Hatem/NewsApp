package com.example.newsapp.viewmodels.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.repo.NewsRepo
import kotlinx.coroutines.launch

class SavedNewsViewModel(
    private val newsRepo: NewsRepo
) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepo.insertArticle(article)
    }

    fun getSavedArticles() = newsRepo.getSavedArticle()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

}