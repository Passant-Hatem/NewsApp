package com.example.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repo.NewsRepo

class NewsViewModelFactory(
    val app : Application,
    private val newsRepo: NewsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app ,newsRepo) as T
    }
}