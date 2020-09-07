package com.example.specialnotes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.specialnotes.R
import com.example.specialnotes.ui.Ui.MainActivity
import com.example.specialnotes.ui.Ui.MainViewModel
import com.example.specialnotes.ui.adapters.NewsAdapter2
import com.example.specialnotes.ui.util.Constants
import com.example.specialnotes.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.specialnotes.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_fragment.*
import kotlinx.android.synthetic.main.fragment_search_fragment.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragmentFragment : Fragment(R.layout.fragment_search_fragment) {

    lateinit var viewModel: MainViewModel
    lateinit var searchAdapter: NewsAdapter2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        searhQuery()
        setUpRecyclerView()

        searchAdapter.setOnItemOnClickListner {
            val bundle = Bundle().apply {
                putSerializable("articles", it)
            }
            findNavController().navigate(
                R.id.action_searchFragmentFragment_to_articleNewsFragment,
                bundle
            )
        }

        viewModelFunction()

    }

    private fun searhQuery() {
        var job: Job? = null
        etSearch.addTextChangedListener { searchQuery ->
            job?.cancel()
            job = MainScope().launch {
                delay(5000)
                searchQuery?.let {
                    if (searchQuery.toString().isNotEmpty()) {
                        viewModel.getSearchingNews(searchQuery.toString())
                    }
                }
            }

        }
    }

    private fun viewModelFunction() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Sucess -> {
                    hideProgressBar()
                    response.data?.let { searchResponse ->
                        searchAdapter.submitList(searchResponse.articles.toList())
                        val totalPages = searchResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPAges == totalPages

                        if (isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("gemmy", it)
                        Toast.makeText(activity,"An error occured: $it",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressbar()
                }
            }

        })
    }

    private fun showProgressbar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = false
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListner = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibaleItemCount = layoutManager.childCount
            val totItemCount = layoutManager.itemCount

            val isNotLoading = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibaleItemCount >= totItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreVisible = totItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoading && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getSearchingNews(etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrolling = true
        }
    }


    private fun setUpRecyclerView() {
        searchAdapter = NewsAdapter2()

        rvSearchNews.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = searchAdapter
            addOnScrollListener(this@SearchFragmentFragment.scrollListner)
        }
    }
}