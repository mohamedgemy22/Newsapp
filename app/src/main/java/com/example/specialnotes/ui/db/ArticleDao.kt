package com.example.specialnotes.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.specialnotes.ui.model.Article

@Dao
abstract class ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    abstract fun getAllArticles():LiveData<List<Article>>

    @Delete
    abstract  suspend fun deleteArticles(article: Article)

}