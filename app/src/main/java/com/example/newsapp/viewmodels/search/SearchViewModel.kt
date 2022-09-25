package com.example.newsapp.viewmodels.search

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApp
import com.example.newsapp.model.News
import com.example.newsapp.repo.NewsRepo
import com.example.newsapp.util.ResponseState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

@Suppress("DEPRECATION")
class SearchViewModel(
    app: Application,
    private val newsRepo: NewsRepo
) : AndroidViewModel(app) {
    //search for news
    val searchNewsList: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var searchPages = 1
    private var loadedSearchRes: News? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null

    fun getSearchRes(searchQuery: String) = viewModelScope.launch {
        safeGetSearchRes(searchQuery)
    }

    private suspend fun safeGetSearchRes(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNewsList.postValue(ResponseState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.searchForNews(searchQuery, searchPages)
                searchNewsList.postValue(handleSearchResponse(response))
            } else {
                searchNewsList.postValue(ResponseState.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNewsList.postValue(ResponseState.Error("Network Failure"))
                else -> searchNewsList.postValue(ResponseState.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchResponse(response: Response<News>): ResponseState<News> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (loadedSearchRes == null || newSearchQuery != oldSearchQuery) {
                    searchPages = 1
                    oldSearchQuery = newSearchQuery
                    loadedSearchRes = resultResponse
                } else {
                    searchPages++
                    val oldArticles = loadedSearchRes?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResponseState.Success(loadedSearchRes ?: resultResponse)
            }
        }
        return ResponseState.Error(response.message())
    }


    //check internet connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}