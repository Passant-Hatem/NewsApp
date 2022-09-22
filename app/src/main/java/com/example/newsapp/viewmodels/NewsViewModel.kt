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

    //get news
    val news: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var newsPages = 1

    init {
        getNews("us")
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        news.postValue(ResponseState.Loading())
        val response = newsRepo.getNews(countryCode ,newsPages)
        news.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<News>): ResponseState<News>{
        if (response.isSuccessful){
            response.body()?.let {
                return ResponseState.Success(it)
            }
        }
        return ResponseState.Error(response.message())
    }

    //search for news
    val searchNewsRes: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var searchPages = 1


    fun getSearchRes(searchQuery: String) = viewModelScope.launch {
        searchNewsRes.postValue(ResponseState.Loading())
        val response = newsRepo.searchForNews(searchQuery ,searchPages)
        news.postValue(handleSearchResponse(response))
    }

    private fun handleSearchResponse(response: Response<News>): ResponseState<News>{
        if (response.isSuccessful){
            response.body()?.let {
                return ResponseState.Success(it)
            }
        }
        return ResponseState.Error(response.message())
    }
}