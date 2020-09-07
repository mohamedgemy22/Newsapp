package com.example.specialnotes.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.specialnotes.R
import com.example.specialnotes.ui.Ui.MainActivity
import com.example.specialnotes.ui.Ui.MainViewModel
import com.example.specialnotes.ui.adapters.NewsAdapter2
import com.example.specialnotes.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.specialnotes.ui.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter2



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        newsAdapter.setOnItemOnClickListner {
            val bundel= Bundle().apply {
                putSerializable("articles",it )
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleNewsFragment,
                bundel
            )
        }

        viewModelFunction()
    }


    private fun viewModelFunction() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { responce ->
            when (responce) {
                is Resource.Sucess -> {
                    hideProgressBar()
                    responce.data?.let { newsResponse ->
                        newsAdapter.submitList(newsResponse.articles.toList())
                        Log.e("gemmy","response is ${newsResponse.articles}")
                         val totalPage= newsResponse.totalResults/ QUERY_PAGE_SIZE+2
                         isLastPage= viewModel.breakingNewsPage==totalPage

                        if (isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    responce.message?.let { message ->
                        Log.e("gemmy", message)
                        Toast.makeText(activity,"An error occured: $message",Toast.LENGTH_LONG).show()

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }



    var isLoading= false
    var isLastPage= false
    var isScrolling= false

    val scrollListner = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager= recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibaleItemCount = layoutManager.childCount
            val totItemCount= layoutManager.itemCount

            val isNotLoading = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition+ visibaleItemCount >= totItemCount
            val isNotAtBeginning= firstVisibleItemPosition>=0
            val isTotalMoreVisible = totItemCount >=QUERY_PAGE_SIZE

            val shouldPaginate= isNotLoading && isAtLastItem && isNotAtBeginning&&
                    isTotalMoreVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling= false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isScrolling= true
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading=true
    }


    private fun setUpRecyclerView() {

        newsAdapter= NewsAdapter2()
        rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter= newsAdapter
            addOnScrollListener(this@BreakingNewsFragment.scrollListner)
        }


    }

}