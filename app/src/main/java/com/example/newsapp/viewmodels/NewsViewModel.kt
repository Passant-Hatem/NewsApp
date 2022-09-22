package com.example.newsapp.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.News
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.util.ResponseState
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(
    val newsRepo: NewsRepo
) : ViewModel() {

    val news: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var newsPages = 1

    init {
        getNews("us")
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        news.postValue(ResponseState.Loading())
        val respons = newsRepo.getNews(countryCode ,newsPages)
        news.postValue(handleNewsResponse(respons))
    }

    private fun handleNewsResponse(response: Response<News>): ResponseState<News>{
        if (response.isSuccessful){
            response.body()?.let {
                return ResponseState.Success(it)
            }
        }
        return ResponseState.Error(response.message())
    }
}