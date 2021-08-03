package com.example.wordhelper

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.DragEvent
import android.view.View
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.util.size
import androidx.core.view.iterator
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_word_test.*
import java.io.File
import kotlin.random.Random

class word_test : AppCompatActivity() {
    lateinit var fileName : ArrayList<String>
    lateinit var fileList : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test)
        addFileList()
        initListener()


    }
    private fun addFileList(){
        fileList = ArrayList<String>()
        val dirPath : String = filesDir.toString()
        var directory : Array<out File>? = File(dirPath).listFiles()
        for(file in directory!!){
            if(file.isFile){
                fileList.add(file.name)
            }
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
            fileName.add(fileList.get(Random.nextInt(0, fileList.size)))
            startTest()
        }
        btnWeektest.setOnClickListener {
            Toast.makeText(this, "비활성화 기능입니다.", Toast.LENGTH_SHORT).show()
            //startTest()
        }
        btnMultitest.setOnClickListener {
            fileName = ArrayList<String>()
            var select : SparseBooleanArray = wordbook.checkedItemPositions
            for(index in 0 .. select.size){
                if(select[index]){
                    fileName.add(fileList[index])
                }
            }
            startTest()
        }
    }
}

