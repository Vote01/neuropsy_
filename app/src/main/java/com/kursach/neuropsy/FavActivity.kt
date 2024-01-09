package com.kursach.neuropsy

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class FavActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)


        DatabaseHelper.initialize(applicationContext)

        drawerLayout = findViewById(R.id.drawerLayout);

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)


        // Слушаем боковую панель
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Обработка выбора пункта меню
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    // Действие для пункта "Глоссарий"
                    val intent = Intent(this, GlossaryActivity::class.java)
                    startActivity(intent)

                    true
                }
                R.id.menu_item2 -> {

                    // Действие для пункта "Избранное"
                    val intent = Intent(this, FavActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_item3 -> {
                    // Действие для пункта "Статьи"
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            } }



        // val dbHelper = DatabaseHelper(applicationContext)
        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        articleList = ArrayList()

        loadArticles()
        Log.d("ArticleAdapter", "Number of articles loaded: ${articleList.size}")

        val adapter = ArticleAdapter(this, R.layout.list_item_article, articleList)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id ->
            // Получите данные о выбранном элементе
            val selected = articleList[position]

            // Создайте Intent для открытия новой активности
            val intent = Intent(this, DetailsActivity::class.java)

            // Передайте данные о выбранном блюде в новую активность
            intent.putExtra("ID", (selected.id).toString())

            // Добавьте другие данные по необходимости

            // Запустите новую активность
            startActivity(intent)
        }


    }

    @SuppressLint("Range")
    private fun loadArticles() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM favourites", null)

       // articleList.clear()

        try {
            while (cursor.moveToNext()) {
                val articlId = cursor.getInt(cursor.getColumnIndex("articl_id"))
                val favoriteArticle = getArticleById(articlId)
                articleList.add(favoriteArticle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        val adapter = ArticleAdapter(this, R.layout.list_item_article, articleList)
        listView.adapter = adapter
    }


    @SuppressLint("Range")
    private fun getArticleById(id: Int): Article {
        val db = dbHelper.readableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            "articles2",
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var Article = Article(0, "", "", "", "", "", null, "")

        try {
            if (cursor.moveToFirst()) {
                val articleId = cursor.getLong(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val tags = cursor.getString(cursor.getColumnIndex("tags"))
                val coverData = cursor.getBlob(cursor.getColumnIndex("cover"))
                val about = cursor.getString(cursor.getColumnIndex("about"))

                Article = Article(articleId.toInt(), title, content, author, date, tags, coverData, about)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        return Article
    }

    fun onUpdateClick(view: View) {
        articleList.clear()
        loadArticles()
        val adapter = ArticleAdapter(this, R.layout.list_item_article, articleList)
        listView.adapter = adapter
    }

}
