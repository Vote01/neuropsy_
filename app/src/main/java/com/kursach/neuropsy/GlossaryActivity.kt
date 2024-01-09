package com.kursach.neuropsy
import DatabaseHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView

class GlossaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var navigationView: NavigationView
    private lateinit var glossaryList: ArrayList<Glossary>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glossary)
        DatabaseHelper.initialize(applicationContext)
       val dbHelper = DatabaseHelper(this)

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

        val listView: ListView = findViewById(R.id.listViewGlossary)
        //
        glossaryList = ArrayList()
        loadGlossary(dbHelper)
        Log.d("GlossaryAdapter", "Number of glossary loaded: ${glossaryList.size}")

        val adapter = GlossaryAdapter(this, R.layout.list_item_glossary, glossaryList)
        listView.adapter = adapter

    }

    @SuppressLint("Range")
    private fun fetchGlossaryTermsFromDatabase(dbHelper: DatabaseHelper): List<String> {
        val glossaryTermsList: MutableList<String> = ArrayList()

        val db = dbHelper.readableDatabase

        val cursor = db.query("glossary", arrayOf("term_name"), null, null, null, null, "term_name")

        try {
            while (cursor.moveToNext()) {
                val termName = cursor.getString(cursor.getColumnIndex("term_name"))
                glossaryTermsList.add(termName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }

        return glossaryTermsList
    }

    @SuppressLint("Range")
    private fun loadGlossary(dbHelper: DatabaseHelper) {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM glossary", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val articleId = cursor.getLong(cursor.getColumnIndex("term_id"))
                val title = cursor.getString(cursor.getColumnIndex("term_name"))
                val content = cursor.getString(cursor.getColumnIndex("description"))
                val tags = cursor.getString(cursor.getColumnIndex("tags"))

                val glossary = Glossary(articleId.toInt(), title, content,tags)

                glossaryList.add(glossary)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
    }



}
