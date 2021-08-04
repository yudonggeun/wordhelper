package com.example.wordhelper

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_word_view.*

class word_view : AppCompatActivity() {
    lateinit var wordList : ArrayList<String>
    lateinit var detailList : ArrayList<List<String>>
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
        wordList = ArrayList()
        detailList = ArrayList()
        testCount = ArrayList()
        failCount = ArrayList()

        var fileName : String? = intent.getStringExtra("fileName")
        initListener()
        addWord(fileName)
        addViewFlipper()
        wordViewTextView.text = "정답률 : ${testCount[index]-failCount[index]}/${testCount[index]}"
    }
    private fun detailToArray(details : String) : List<String> {
        return details.split("^")
    }
    private fun addWord(fileName : String?){
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery("SELECT word, detail, testCount, failCount FROM Word WHERE fileName is '$fileName';", null)
        size = 0;
        while(cursor.moveToNext()){
            wordList.add(cursor.getString(0))
            detailList.add(detailToArray(cursor.getString(1)))
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