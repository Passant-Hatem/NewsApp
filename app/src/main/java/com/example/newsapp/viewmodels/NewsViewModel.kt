package com.example.newsapp.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.util.ResponseState
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(
    val newsRepo: NewsRepo
) : ViewModel() {

    //get news
    val newsList: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var newsPages = 1
    var loadedNews: News? = null

    init {
        getNews("us")
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        newsList.postValue(ResponseState.Loading())
        val response = newsRepo.getNews(countryCode ,newsPages)
        newsList.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<News>): ResponseState<News>{
        if (response.isSuccessful){
            response.body()?.let {
                newsPages++
                if (loadedNews == null){
                    loadedNews = it
                }else{
                    val oldArticles = loadedNews?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResponseState.Success(loadedNews ?: it)
            }
        }
        return ResponseState.Error(response.message())
    }

    //search for news
    val searchNewsList: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var searchPages = 1
    var loadedSearchRes: News? = null


    fun getSearchRes(searchQuery: String) = viewModelScope.launch {
        searchNewsList.postValue(ResponseState.Loading())
        val response = newsRepo.searchForNews(searchQuery ,searchPages)
        newsList.postValue(handleSearchResponse(response))
    }

    private fun handleSearchResponse(response: Response<News>): ResponseState<News>{
        if (response.isSuccessful){
            response.body()?.let {
                searchPages++
                if (loadedSearchRes == null){
                    loadedSearchRes = it
                }else{
                    val oldArticles = loadedSearchRes?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResponseState.Success(loadedSearchRes ?: it)
            }
        }
        return ResponseState.Error(response.message())

    }

    // insert, get, and delete from database
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepo.insertArticle(article)
    }

    fun getSavedArticles() = newsRepo.getSavedArticle()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

}