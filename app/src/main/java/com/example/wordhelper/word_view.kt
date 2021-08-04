package com.example.wordhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.wordhelper.wordView
import kotlinx.android.synthetic.main.activity_word_test.*
import kotlinx.android.synthetic.main.activity_word_test_activity.*
import kotlinx.android.synthetic.main.activity_word_test_activity.wordView
import kotlinx.android.synthetic.main.activity_word_view.*
import java.io.File

class word_view : AppCompatActivity() {
    lateinit var wordList : ArrayList<String>
    lateinit var detailList : ArrayList<ArrayList<String>>
    lateinit var testCount : ArrayList<Int>
    lateinit var failCount : ArrayList<Int>
    lateinit var wordViewList : ArrayList<wordView>
    lateinit var wordDBHelper: SQLiteOpenHelper
    lateinit var sqlDB : SQLiteDatabase
    var size : Int = 0
    var index : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_view)
        wordDBHelper = wordDBHelper(this)
        var fileName : String? = intent.getStringExtra("fileName")
        initListener()
        addWord(fileName)
        addViewFlipper()
        wordViewTextView.text = "정답률 : ${testCount[index]-failCount[index]}/${testCount[index]}"
    }
    private fun addWord(fileName : String?){
        sqlDB = wordDBHelper.readableDatabase
        wordList = ArrayList()
        detailList = ArrayList()
        testCount = ArrayList()
        failCount = ArrayList()
        var cursor = sqlDB.rawQuery("SELECT word, detail, testCount, failCount FROM Word WHERE fileName is '$fileName';", null)
        size = 0;
        while(cursor.moveToNext()){
            var details : ArrayList<String> = ArrayList()
            wordList.add(cursor.getString(0))
            details.add(cursor.getString(1))
            detailList.add(details)
            testCount.add(cursor.getInt(2))
            failCount.add(cursor.getInt(3))
        }
    }
    private fun addViewFlipper(){
        wordViewList = ArrayList()
        for(index in wordList.indices){
            val frameLayout = wordView(this)
            frameLayout.addWord(wordList[index])
            frameLayout.addDetail(detailList[index])
            frameLayout.showDetail()
            wordLearnView.addView(frameLayout)
            wordViewList.add(frameLayout)
            size++
        }
    }
    private fun initListener(){
        wordLearnView.setOnClickListener {
            wordLearnView.showNext()
            index=(index+1)%size
            wordViewTextView.text = "정답률 : ${testCount[index]-failCount[index]}/${testCount[index]}"
        }
    }

}