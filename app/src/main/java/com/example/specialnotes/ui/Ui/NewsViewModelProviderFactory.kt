package com.example.specialnotes.ui.Ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.specialnotes.ui.repositories.NewsRepository

class NewsViewModelProviderFactory(
    val app : Application,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return   MainViewModel(newsRepository,app) as T
    }

}