package com.example.wordhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class wordDBHelper(context: Context) : SQLiteOpenHelper(context, "Word", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("Create table IF NOT EXISTS fileList(fileName CHAR(20) Primary key);")
        db!!.execSQL("Create table IF NOT EXISTS Word(fileName CHAR(20), word CHAR(20), detail CHAR(150), testCount INTEGER, failCount INTEGER );")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS fileList")
    }
}