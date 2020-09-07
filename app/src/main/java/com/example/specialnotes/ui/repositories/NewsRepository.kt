package com.example.specialnotes.ui.repositories

import com.example.specialnotes.ui.NewsApi.NewsApi
import com.example.specialnotes.ui.NewsApi.RetrofitInstance
import com.example.specialnotes.ui.db.ArticleDatabase
import com.example.specialnotes.ui.model.Article
import retrofit2.Retrofit

class NewsRepository(val bd: ArticleDatabase) {

    suspend fun getAllBreakingNews(countryCode : String , PageNumber:Int)=
     RetrofitInstance.api.breakingNews(countryCode,PageNumber)

    suspend fun searchAllRequest(string: String, pageNumber: Int)=
        RetrofitInstance.api.searchinNews(string,pageNumber)

    suspend fun addArticlesinDatabase(article: Article)=
        bd.articleDao().insert(article)

    fun getAllArticles()=
        bd.articleDao().getAllArticles()

    suspend fun deleteArticleFromDatabase(article: Article)=
        bd.articleDao().deleteArticles(article)

}