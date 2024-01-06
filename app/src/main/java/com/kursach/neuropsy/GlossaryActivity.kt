package com.kursach.neuropsy
import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class GlossaryActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glossary)

        val dbHelper = DatabaseHelper(this)

        // Fetch glossary terms from the database
        val glossaryTermsList = fetchGlossaryTermsFromDatabase(dbHelper)

        val listView: ListView = findViewById(R.id.listViewGlossary)

        // Creating an ArrayAdapter to display glossary terms in the ListView
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, glossaryTermsList)
        listView.adapter = arrayAdapter
    }

    @SuppressLint("Range")
    private fun fetchGlossaryTermsFromDatabase(dbHelper: DatabaseHelper): List<String> {
        val glossaryTermsList: MutableList<String> = ArrayList()

        val db = dbHelper.readableDatabase

        // Assuming your glossary table is named "glossary" and the term_name column contains glossary terms
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

}
