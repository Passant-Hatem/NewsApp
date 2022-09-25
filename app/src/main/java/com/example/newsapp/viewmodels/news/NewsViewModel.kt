package com.example.newsapp.viewmodels.news


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
import com.example.newsapp.util.Constants.Companion.COUNTRY_CODE
import com.example.newsapp.util.ResponseState
import kotlinx.coroutines.launch
import retrofit2.Response
import okio.IOException

@Suppress("DEPRECATION")
class NewsViewModel(
    app: Application,
    private val newsRepo: NewsRepo
) : AndroidViewModel(app) {

    //get news
    val newsList: MutableLiveData<ResponseState<News>> = MutableLiveData()
    var newsPages = 1
    private var loadedNews: News? = null

    init {
        getNews(COUNTRY_CODE)
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        safeNewsCall(countryCode)
    }

    private suspend fun safeNewsCall(countryCode: String) {
        newsList.postValue(ResponseState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getNews(countryCode, newsPages)
                newsList.postValue(handleNewsResponse(response))
            } else {
                newsList.postValue(ResponseState.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsList.postValue(ResponseState.Error("Network Failure"))
                else -> newsList.postValue(ResponseState.Error("Conversion Error"))
            }
        }
    }

    private fun handleNewsResponse(response: Response<News>): ResponseState<News> {
        if (response.isSuccessful) {
            response.body()?.let {
                newsPages++
                if (loadedNews == null) {
                    loadedNews = it
                } else {
                    val oldArticles = loadedNews?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResponseState.Success(loadedNews ?: it)
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