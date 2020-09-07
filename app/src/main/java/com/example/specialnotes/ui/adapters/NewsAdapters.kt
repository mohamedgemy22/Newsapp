package com.example.specialnotes.ui.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.specialnotes.R
import com.example.specialnotes.ui.model.Article
import kotlinx.android.synthetic.main.article_preview.view.*

//class NewsAdapters: RecyclerView.Adapter<NewsAdapters.NewsViewHolder>() {
//
//    inner class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
//
//    private val differCallback= object : DiffUtil.ItemCallback<Article>(){
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//         return oldItem.url == newItem.url
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//         return oldItem == newItem
//        }
//    }
//     val differ= AsyncListDiffer(this, differCallback)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//         return NewsViewHolder(
//             LayoutInflater.from(parent.context).inflate(R.layout.article_preview,parent,false)
//         )
//    }
//
//    override fun getItemCount(): Int {
//         return  differ.currentList.size
//    }
//
//    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//     val article= differ.currentList[position]
//      holder.itemView.apply {
//          Glide.with(this).load(article.urlToImage).into(ivArticleImage)
//          tvSource.text= article.source.name
//          tvTitle.text= article.title
//          tvDescription.text= article.description
//          tvPublishedAt.text = article.publishedAt
//          setOnClickListener {
//            onItemClickListner?.let { it(article)}
//         }
//      }
//    }
//
//    private var onItemClickListner : ((Article) -> Unit)?= null
//
//    fun setOnItemOnClickListner(listner: (Article)-> Unit){
//        onItemClickListner= listner
//    }
//}