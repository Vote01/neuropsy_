package com.kursach.neuropsy

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArticlesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ArticleAdapter()
        recyclerView.adapter = adapter
        loadArticles()
        return view
    }

    object ArticleContract {
        // Класс, представляющий схему таблицы
        object ArticleEntry : BaseColumns {
            const val TABLE_NAME = "articles"
            const val COLUMN_TITLE = "title"
            const val COLUMN_COVER = "cover"
            const val COLUMN_CONTENT = "content"
            const val COLUMN_AUTHOR = "author"
            const val COLUMN_DATE = "date"
            const val COLUMN_TAGS = "tags"
            const val COLUMN_ABOUT = "about"
        }
    }

    @SuppressLint("Range")
    private fun loadArticles() {
        val dbHelper = ArticleDbHelper(requireContext())
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            ArticleContract.ArticleEntry.COLUMN_TITLE,
            ArticleContract.ArticleEntry.COLUMN_COVER,
            ArticleContract.ArticleEntry.COLUMN_ABOUT
        )

        val sortOrder = ArticleContract.ArticleEntry.COLUMN_DATE + " DESC"

        val cursor = db.query(
            ArticleContract.ArticleEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val articles = mutableListOf<Article>()

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE))
            val coverUri = cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_COVER))
            val about = cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ABOUT))

            val article = Article(title, coverUri, about)
            articles.add(article)
        }

        cursor.close()
        adapter.setArticles(articles)
    }




}
