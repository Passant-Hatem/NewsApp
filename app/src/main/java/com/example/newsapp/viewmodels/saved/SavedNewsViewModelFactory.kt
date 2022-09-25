package com.example.newsapp.viewmodels.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repo.NewsRepo

class SavedNewsViewModelFactory(
    private val newsRepo: NewsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedNewsViewModel(newsRepo) as T
    }
}