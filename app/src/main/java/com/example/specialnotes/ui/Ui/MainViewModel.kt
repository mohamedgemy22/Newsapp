package com.example.specialnotes.ui.Ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.example.specialnotes.ui.NewsApplicationClass
import com.example.specialnotes.ui.model.Article
import com.example.specialnotes.ui.model.NewsResponse
import com.example.specialnotes.ui.repositories.NewsRepository
import com.example.specialnotes.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel(
    val newsRepository: NewsRepository,
     app:Application
): AndroidViewModel(app) {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var  breakingNewsRespponse:NewsResponse?= null


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPAges=1
    var searchingNewsRespponse:NewsResponse?= null


    init {
        getBreakingNews("eg")
    }

    fun getBreakingNews(countrycode: String)= viewModelScope.launch {
     safeBreakingNewsCall(countrycode)
    }

    fun getSearchingNews(SearchQuery: String)= viewModelScope.launch {
       safeSearchingNewsCall(SearchQuery)
    }

    private fun handeleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if (breakingNewsRespponse ==null){
                    breakingNewsRespponse= resultResponse
                }else{
                    val oldArticles= breakingNewsRespponse?.articles
                    val newsResponse= resultResponse.articles
                    oldArticles?.addAll(newsResponse)
                }

                return Resource.Sucess(breakingNewsRespponse?:resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    private fun handelSearchingNewsRsponseStrin(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPAges++
                if (searchingNewsRespponse ==null){
                    searchingNewsRespponse= resultResponse
                }else{
                    val oldArticles= searchingNewsRespponse?.articles
                    val newsResponse= resultResponse.articles
                    oldArticles?.addAll(newsResponse)
                }

                return Resource.Sucess(searchingNewsRespponse?:resultResponse)
            }
        }
        return  Resource.Error(response.message())
    }

    fun  saveArticles(article: Article)= viewModelScope.launch {
        newsRepository.addArticlesinDatabase(article)
    }

    fun getAllArticl()= newsRepository.getAllArticles()

    fun deleteArticles(article: Article)= viewModelScope.launch {
        newsRepository.deleteArticleFromDatabase(article)
    }


    private suspend fun safeSearchingNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response= newsRepository.searchAllRequest(searchQuery, searchNewsPAges)
                searchNews.postValue(handelSearchingNewsRsponseStrin(response))
            }else{
                searchNews.postValue(Resource.Error("No Interent Connction"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Newtwork Failuer"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countrycode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getAllBreakingNews(countrycode, breakingNewsPage)
                breakingNews.postValue(handeleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("No Interent Connction"))
            }
        }catch (t: Throwable){
             when(t){
                 is IOException -> breakingNews.postValue(Resource.Error("Newtwork Failuer"))
                 else -> breakingNews.postValue(Resource.Error("Conversion Error"))
             }
        }
    }


    private fun hasInternetConnection():Boolean{
      val connectManager = getApplication<NewsApplicationClass>().getSystemService(
          Context.CONNECTIVITY_SERVICE
      )as ConnectivityManager
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
          val activityNetwork = connectManager.activeNetwork?: return false
          val capaplities= connectManager.getNetworkCapabilities(activityNetwork)?:return false
          return when{
              capaplities.hasTransport(TRANSPORT_WIFI)->true
              capaplities.hasTransport(TRANSPORT_CELLULAR)->true
              capaplities.hasTransport(TRANSPORT_ETHERNET)->true
              else-> false
          }
      }else{
          connectManager.activeNetworkInfo?.run {
              return when (type){
                  TYPE_WIFI -> true
                  TYPE_MOBILE -> true
                  TYPE_ETHERNET -> true
                  else -> false
              }
          }
      }
        return false
    }







}