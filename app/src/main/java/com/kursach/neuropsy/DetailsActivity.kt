package com.kursach.neuropsy

import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var ArticleList: ArrayList<Article>
    private var articleId: Int = 0

    private lateinit var favoritesButton: ImageButton    // звезда
    private var isFavorite: Boolean = false

    private var selectedDish: Long = 0  // Добавлено объявление переменной selectedDish


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = DatabaseHelper(this)
        ArticleList = ArrayList()

        val intent = intent
        val dishName = intent.getStringExtra("dishName")
        val description = intent.getStringExtra("description")
        val dishId2 = intent.getStringExtra("ID")


        if (dishId2 != null) {
            articleId = dishId2.toInt()
        }

        // Заполнение объекта selectedDish
        selectedDish = articleId.toLong()






        favoritesButton = findViewById(R.id.favoritesButton)
        favoritesButton.setOnClickListener {
            toggleFavoriteStatus(selectedDish)
        }

        // Вызов метода загрузки блюда
        loadDish()

        // Отобразите другие данные по необходимости
    }



    private fun toggleFavoriteStatus(selectedDish: Long) {

      //  isFavorite = !isFavorite


        val databaseHelper = DatabaseHelper(this) // Замените "this" на ваш контекст



        if (isDishInFavorites(articleId.toLong())) {
          isFavorite = true


            // Если в избранном, добавьте идентификатор в таблицу
            favoritesButton.setImageResource(R.drawable.n444)
            // Установите одну картинку

        } else {
            isFavorite = !isFavorite
            // Установите другую картинку
            favoritesButton.setImageResource(R.drawable.n555)


            // Ваш код для замены изображения
            // Например:
            // dish.setImageData(newImageData)
        }


        if (!isFavorite) {
            // Если в избранном, добавьте идентификатор в таблицу
            databaseHelper.addToFavorites(selectedDish)
            // Установите одну картинку
            favoritesButton.setImageResource(R.drawable.n444)
        } else {
            // Если не в избранном, удалите идентификатор из таблицы
            databaseHelper.removeFromFavorites(selectedDish)
            // Установите другую картинку
            favoritesButton.setImageResource(R.drawable.n555)
        }
        // Дополнительный код для сохранения статуса в базе данных или другом хранилище
    }


    @SuppressLint("Range")
    private fun loadDish() {
        val db = dbHelper.readableDatabase
        val id = arrayOf(articleId.toString())
        val cursor: Cursor = db.rawQuery("SELECT * FROM articles2 WHERE id = ?", id)

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


                val titleTextView: TextView = findViewById(R.id.textViewTitle)
                val contentTextView: TextView = findViewById(R.id.textViewContent)
                val authorTextView: TextView = findViewById(R.id.textViewAuthor)
                val dateTextView: TextView = findViewById(R.id.textViewDate)
                val tagsTextView: TextView = findViewById(R.id.textViewTags)
                val coverImageView: ImageView = findViewById(R.id.imageViewCover)
                val aboutTextView: TextView = findViewById(R.id.textViewAbout)


                // Отобразите данные в представлениях
                titleTextView.text = title
                contentTextView.text = content
                authorTextView.text = author
                dateTextView.text = date
                tagsTextView.text = tags
                aboutTextView.text = about



                // Опциональная проверка наличия в "favourites" и замена изображения
                if (isDishInFavorites(articleId)) {
                    isFavorite = true


                        // Если в избранном, добавьте идентификатор в таблицу
                        favoritesButton.setImageResource(R.drawable.n444)
                        // Установите одну картинку

                    } else {
                        isFavorite = !isFavorite
                        // Установите другую картинку
                        favoritesButton.setImageResource(R.drawable.n555)


                    // Ваш код для замены изображения
                    // Например:
                    // dish.setImageData(newImageData)
                }

                ArticleList.add(article)

                cursor.moveToNext()

//
//                // Отобразите данные в представлениях
//                val textViewCookingTime: TextView = findViewById(R.id.textViewCookingTime)
//                val textViewStepDescription: TextView = findViewById(R.id.textViewStepDescription)
//
//                val textViewIngredients: TextView = findViewById(R.id.textViewIngredients)
//                val imageViewDish: ImageView = findViewById(R.id.imageViewDish)
//                val textViewCategory: TextView = findViewById(R.id.textViewCategory)
//

//                textViewCookingTime.text = "Время готовки: $cookingTime минут"
//                textViewStepDescription.text = step_description
//                textViewIngredients.text = ingredients

//                textViewCategory.text = "Категория блюда: $category "

                // Отобразите изображение, если оно есть
                if (coverData != null && coverData.isNotEmpty()) {
                    val bitmap = BitmapFactory.decodeByteArray(coverData, 0, coverData.size)
                    coverImageView.setImageBitmap(bitmap)
                }

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }
    private fun isDishInFavorites(dishId: Long): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "articl_id = ?"
        val selectionArgs = arrayOf(dishId.toString())
        val cursor: Cursor = db.query("favourites", null, selection, selectionArgs, null, null, null)

        val isDishInFavorites = cursor.moveToFirst()
        cursor.close()
        db.close()

        return isDishInFavorites
    }


}
