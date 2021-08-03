package com.example.wordhelper

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_word_manage.*
import kotlinx.android.synthetic.main.activity_word_test.*
import java.io.File

class word_manage : AppCompatActivity() {
    lateinit var dbHelper: SQLiteOpenHelper
    lateinit var wordDB : SQLiteDatabase
    lateinit var fileList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_manage)

        dbHelper = wordDBHelper(this)
        addFileList()
        initBtnListener()

    }
    private fun addFileList(){
        fileList = ArrayList<String>()
        wordDB = dbHelper.readableDatabase
        var cursor = wordDB!!.rawQuery("SELECT * FROM fileList", null);
        while(cursor.moveToNext()){
            fileList.add(cursor.getString(0))
        }
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList)
        wordCorrectionList.adapter = adapter
    }
    fun initBtnListener(){
        btnWordAdd.setOnClickListener {

        }
        btnWordDelete.setOnClickListener {

        }
        btnEditAndView.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_view::class.java)
            startActivity(intent)
        }
    }
    inner class wordDBHelper(context: Context) : SQLiteOpenHelper(context, "groupDB", null, 1){
        override fun onCreate(db: SQLiteDatabase?) {
            true
        }
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS Word")
        }
        fun addWord(db : SQLiteDatabase?,fileName : String, word : String, detail : String){
            db!!.execSQL("INSERT INTO Word VALUES($fileName, $word, $detail, 0, 0);")
        }
        fun deleteWords(db: SQLiteDatabase?, fileName : String){
            db!!.execSQL("DELETE FROM Word WHERE fileName is $fileName;")
        }

    }
}