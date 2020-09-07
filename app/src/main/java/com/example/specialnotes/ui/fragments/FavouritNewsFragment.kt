package com.example.specialnotes.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.specialnotes.R
import com.example.specialnotes.ui.Ui.MainActivity
import com.example.specialnotes.ui.Ui.MainViewModel
import com.example.specialnotes.ui.adapters.NewsAdapter2
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favourit_news.*

class FavouritNewsFragment : Fragment(R.layout.fragment_favourit_news) {

    lateinit var viewModel: MainViewModel
    lateinit var favouritAdapter: NewsAdapter2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as MainActivity).viewModel
        setUpRecyclerView()

        val itemTouchHelper= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return  true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article= favouritAdapter.currentList[position]
                viewModel.deleteArticles(article)
                Snackbar.make(view, "Article is deleted ... ",Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        viewModel.saveArticles(article)
                    }
                    show()
                }
            }

        }


        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(rvSavedNews)
        }



        viewModel.getAllArticl().observe(viewLifecycleOwner, Observer {articles->
            favouritAdapter.submitList(articles)

        })


    }

    private fun setUpRecyclerView() {
        favouritAdapter=NewsAdapter2()

        rvSavedNews.apply {
            layoutManager= LinearLayoutManager(this.context)
            adapter=favouritAdapter
        }


    }
}