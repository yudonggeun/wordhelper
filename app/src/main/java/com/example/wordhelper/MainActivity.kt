package com.example.wordhelper

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dbHelper = wordDBHelper(this)
        dbHelper.onCreate(dbHelper.writableDatabase)

        btnWordtest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test::class.java)
            startActivity(intent);
        }
        btnWordmanage.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_manage::class.java)
            startActivity(intent);
        }
    }

    inner class wordDBHelper(context: Context) : SQLiteOpenHelper(context, "groupDB", null, 1){
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("Create table IF NOT EXISTS fileList(fileName CHAR(20) Primary key);")
            db!!.execSQL("Create table IF NOT EXISTS Word(fileName CHAR(20), word CHAR(20), detail CHAR(150), testCount INTEGER, failCount INTEGER );")
        }
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS fileList")
        }

    }
}