package com.kursach.neuropsy

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var articleList: ArrayList<Article>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        DatabaseHelper.initialize(applicationContext)

        drawerLayout = findViewById(R.id.drawerLayout);

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//
//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

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
                    true
                }
                R.id.menu_item3 -> {
                    // Действие для пункта "Тесты"
                    true
                }
                R.id.menu_item4 -> {
                    // Действие для пункта "Поиск по тегам"
                    true
                }
                else -> false
            } }



        // val dbHelper = DatabaseHelper(applicationContext)
        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        articleList = ArrayList()

        loadArticles()
        Log.d("DishAdapter", "Number of dishes loaded: ${articleList.size}")

        val adapter = ArticleAdapter(this, R.layout.list_item_article, articleList)
        listView.adapter = adapter


        listView.setOnItemClickListener { parent, view, position, id ->
            // Получите данные о выбранном элементе
            val selectedDish = articleList[position]

            // Создайте Intent для открытия новой активности
            val intent = Intent(this, DetailsActivity::class.java)

            // Передайте данные о выбранном блюде в новую активность
            intent.putExtra("ID", (selectedDish.id).toString())
//            intent.putExtra("description", selectedDish.description)
//            intent.putExtra("dishid", (selectedDish.dishId).toString())
//            intent.putExtra("cookingTime", selectedDish.cookingTime)

            // Добавьте другие данные по необходимости

            // Запустите новую активность
            startActivity(intent)
        }


    }

    @SuppressLint("Range")
    private fun loadArticles() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM articles2", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val articleId = cursor.getLong(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val author = cursor.getString(cursor.getColumnIndex("author"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val tags = cursor.getString(cursor.getColumnIndex("tags"))
                val coverData = cursor.getBlob(cursor.getColumnIndex("cover"))
                val about = cursor.getString(cursor.getColumnIndex("about"))

                val article = Article(articleId.toInt(), title, content, author, date, tags, coverData, about)

                articleList.add(article)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }

    fun onBTClick(view: View) {
        val intent = Intent(this, GlossaryActivity::class.java)
        startActivity(intent)
    }

    fun onBTClickRE(view: View) {
        // Your implementation for the button click
        val intent = Intent(this, GlossaryActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("Range")
    fun onAddRecipeClick(item: MenuItem) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM favourites", null)

        articleList.clear()

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
    private fun getArticleById(dishId: Int): Article {
        val db = dbHelper.readableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(dishId.toString())

        val cursor = db.query(
            "articles2",
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var Article = Article(0, "", "", "", "", "", null, "") // Значение по умолчанию, если блюдо не найдено

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
        // Очистите текущий список блюд
        articleList.clear()

        // Повторно загрузите данные из базы данных
        loadArticles()

        // Создайте новый адаптер с обновленным списком блюд
        val adapter = ArticleAdapter(this, R.layout.list_item_article, articleList)

        // Установите новый адаптер для вашего ListView
        listView.adapter = adapter
    }






}
