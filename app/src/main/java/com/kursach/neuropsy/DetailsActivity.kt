package com.kursach.neuropsy

import DatabaseHelper
import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var ArticleList: ArrayList<Article>
    private var articleId: Int = 0

    private lateinit var favoritesButton: ImageButton
    private var isFavorite: Boolean = false

    private var selected: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = DatabaseHelper(this)
        ArticleList = ArrayList()

        val intent = intent
        val aId = intent.getStringExtra("ID")


        if (aId != null) {
            articleId = aId.toInt()
        }

        selected = articleId.toLong()

        favoritesButton = findViewById(R.id.favoritesButton)
        favoritesButton.setOnClickListener {
            toggleFavoriteStatus(selected)
        }

        loadArcticle()
    }



    private fun toggleFavoriteStatus(selected: Long) {

        val databaseHelper = DatabaseHelper(this)

        if (inFavorites(articleId.toLong())) {
            isFavorite = true

            favoritesButton.setImageResource(R.drawable.izbr_64)

        } else {
            isFavorite = !isFavorite
            favoritesButton.setImageResource(R.drawable.nizbr_64)

        }


        if (!isFavorite) {
            databaseHelper.addToFavorites(selected)
            favoritesButton.setImageResource(R.drawable.izbr_64)
        } else {
            databaseHelper.removeFromFavorites(selected)
            favoritesButton.setImageResource(R.drawable.nizbr_64)
        }
    }


    @SuppressLint("Range")
    private fun loadArcticle() {
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
              //  val authorTextView: TextView = findViewById(R.id.textViewAuthor)
                val dateTextView: TextView = findViewById(R.id.textViewDate)
                val tagsTextView: TextView = findViewById(R.id.textViewTags)
                val coverImageView: ImageView = findViewById(R.id.imageViewCover)
                    //       val aboutTextView: TextView = findViewById(R.id.textViewAbout)


                // Отобразите данные в представлениях
                titleTextView.text = title
                contentTextView.text = content
                    //  authorTextView.text = author
                dateTextView.setText("Дата: " + date);
               // dateTextView.text = date
                    //   tagsTextView.text = tags
                tagsTextView.setText("Тэги: " + tags);
           //     aboutTextView.text = about

                // Опциональная проверка наличия в "favourites" и замена изображения
                if (inFavorites(articleId)) {
                    isFavorite = true


                    // Если в избранном, добавьте идентификатор в таблицу
                    favoritesButton.setImageResource(R.drawable.izbr_64)
                    // Установите одну картинку

                } else {
                    isFavorite = !isFavorite
                    favoritesButton.setImageResource(R.drawable.nizbr_64)

                }

                ArticleList.add(article)

                cursor.moveToNext()

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
    private fun inFavorites(id: Long): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "articl_id = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor: Cursor = db.query("favourites", null, selection, selectionArgs, null, null, null)

        val isDishInFavorites = cursor.moveToFirst()
        cursor.close()
        db.close()

        return isDishInFavorites
    }


}
