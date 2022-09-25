package com.example.newsapp.viewmodels.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repo.NewsRepo

class SearchViwModelFactory(
    val app: Application,
    private val newsRepo: NewsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(app, newsRepo) as T
    }
}