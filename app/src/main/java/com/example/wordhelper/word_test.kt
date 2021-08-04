package com.example.wordhelper

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.util.size
import kotlinx.android.synthetic.main.activity_word_test.*
import kotlin.random.Random

class word_test : AppCompatActivity() {
    lateinit var fileName : ArrayList<String>
    lateinit var fileList : ArrayList<String>
    lateinit var wordDBHelper: SQLiteOpenHelper
    lateinit var sqlDB : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test)
        wordDBHelper = wordDBHelper(this)
        addWordFileList()
        initListener()
    }
    private fun addWordFileList(){
        fileList = ArrayList()
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery("SELECT fileName FROM fileList", null)
        while(cursor.moveToNext()){
            fileList.add(cursor.getString(0))
        }
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, fileList)
        wordbook.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        wordbook.adapter = adapter
    }
    private fun startTest(){
        var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
        intent.putStringArrayListExtra("file", fileName)
        startActivity(intent);
    }
    private fun initListener(){
        wordbook.setOnItemLongClickListener { parent, view, position, id ->
            fileName = ArrayList()
            fileName.add(fileList.get(position))
            startTest()
            true
        }
        btnRandomtest.setOnClickListener {
            fileName = ArrayList<String>()
            if(fileList.size == 0)
                Toast.makeText(this, "단어 리스트를 추가해주세요", Toast.LENGTH_SHORT).show()
            else{
                fileName.add(fileList.get(Random.nextInt(0, fileList.size)))
                startTest()
            }
        }
        btnWeektest.setOnClickListener {
            if(fileList.size == 0)
                Toast.makeText(this, "단어 리스트를 추가해주세요", Toast.LENGTH_SHORT).show()
            else {
                fileName = ArrayList()
                fileName.add("weak")
                startTest()
            }
        }
        btnMultitest.setOnClickListener {
            if(fileList.size == 0)
                Toast.makeText(this, "단어 리스트를 추가해주세요", Toast.LENGTH_SHORT).show()
            else {
                fileName = ArrayList<String>()
                var select: SparseBooleanArray = wordbook.checkedItemPositions
                for (index in 0..select.size) {
                    if (select[index]) {
                        fileName.add(fileList[index])
                    }
                }
                startTest()
            }
        }
    }
}

