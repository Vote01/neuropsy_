package com.kursach.neuropsy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArticleDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "neuropsy_android.db";
    private static final int DATABASE_VERSION = 1;

    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticlesFragment.ArticleContract.ArticleEntry.TABLE_NAME + " ("
                + ArticlesFragment.ArticleContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArticlesFragment.ArticleContract.ArticleEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + ArticlesFragment.ArticleContract.ArticleEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + ArticlesFragment.ArticleContract.ArticleEntry.COLUMN_CONTENT + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticlesFragment.ArticleContract.ArticleEntry.TABLE_NAME);
        onCreate(db);
    }
}