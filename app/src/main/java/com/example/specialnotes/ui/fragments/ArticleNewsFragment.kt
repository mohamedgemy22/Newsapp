package com.example.specialnotes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.specialnotes.R
import com.example.specialnotes.ui.Ui.MainActivity
import com.example.specialnotes.ui.Ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article_news.*


class ArticleNewsFragment : Fragment(R.layout.fragment_article_news) {
    lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val args= ArticleNewsFragmentArgs.fromBundle(arguments!!)
        val article= args.articles

        webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener {
            viewModel.saveArticles(article)
            Snackbar.make(view,"Article saved Suuccssfully",Snackbar.LENGTH_SHORT).show()
        }


    }
}