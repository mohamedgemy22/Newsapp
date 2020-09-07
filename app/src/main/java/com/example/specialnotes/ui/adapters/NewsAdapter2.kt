package com.example.specialnotes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.specialnotes.R
import com.example.specialnotes.ui.model.Article
import kotlinx.android.synthetic.main.article_preview.view.*

class NewsAdapter2(): ListAdapter<Article, NewsAdapter2.NewsViewHolder2>(NewsDiffUtilCallback()) {

    class NewsViewHolder2(itemView: View):RecyclerView.ViewHolder(itemView){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder2 {
        val inflator =LayoutInflater.from(parent.context)
        return NewsViewHolder2(inflator.inflate(R.layout.article_preview,parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder2, position: Int) {
        val article= getItem(position)
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text= article.source?.name
            tvTitle.text= article.title
            tvDescription.text= article.description
            tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListner?.let { it(article) }
            }
        }
    }

    private var onItemClickListner : ((Article) -> Unit)?= null

    fun setOnItemOnClickListner(listner: (Article)-> Unit){
        onItemClickListner= listner
    }
}

class NewsDiffUtilCallback:DiffUtil.ItemCallback<Article>(){
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url==newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }


}