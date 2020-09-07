package com.example.specialnotes.ui.NewsApi

import com.example.specialnotes.ui.model.NewsResponse
import com.example.specialnotes.ui.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun breakingNews(
        @Query("country")
        countryCode:String ="eg",
       @Query("page")
       page:Int=1,
        @Query("apiKey")
        apiKey:String= API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchinNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        page: Int=1,
        @Query("apiKey")
        apiKey: String= API_KEY
    ):Response<NewsResponse>

}