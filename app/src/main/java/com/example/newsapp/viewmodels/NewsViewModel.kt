package com.example.newsapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.newsapp.repo.NewsRepo

class NewsViewModel(
    val newsRepo: NewsRepo
) : ViewModel() {

}