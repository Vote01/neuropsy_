package com.kursach.neuropsy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.ListView
import kotlinx.android.synthetic.main.item_article.view.aboutTextView
import kotlinx.android.synthetic.main.item_article.view.coverImageView
import kotlinx.android.synthetic.main.item_article.view.titleTextView


data class Article(val title: String, val coverUri: String, val about: String)
class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val articles = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(articleList: List<Article>) {
        articles.clear()
        articles.addAll(articleList)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind(article: Article) {
            itemView.titleTextView.text = article.title
            Glide.with(itemView.context).load(article.coverUri).into(itemView.coverImageView)
            itemView.aboutTextView.text = article.about

            itemView.setOnClickListener {
                // Открыть полную версию статьи или выполнить другие действия
            }
        }
    }
}