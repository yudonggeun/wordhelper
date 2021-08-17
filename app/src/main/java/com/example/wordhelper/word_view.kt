package com.example.wordhelper

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_word_test_activity.*
import kotlinx.android.synthetic.main.activity_word_view.*

class word_view : AppCompatActivity() {
    lateinit var wordList : ArrayList<String>
    lateinit var detailList : ArrayList<List<String>>
    lateinit var testCount : ArrayList<Int>
    lateinit var failCount : ArrayList<Int>
    lateinit var wordViewList : ArrayList<wordView>
    lateinit var wordDBHelper: SQLiteOpenHelper
    lateinit var sqlDB : SQLiteDatabase
    var isMovingToLeft : Boolean = true
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
    private fun addWord(fileName : String?){
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery("SELECT word, detail, testCount, failCount, turn FROM Word WHERE fileName is '$fileName' order by word, turn;", null)
        var detail : ArrayList<String>? = null
        while(cursor.moveToNext()){
            if(cursor.getInt(4) == 1) {
                detail = ArrayList()
                wordList.add(cursor.getString(0))
                detailList.add(detail)
                testCount.add(cursor.getInt(2))
                failCount.add(cursor.getInt(3))
            }
            detail!!.add(cursor.getString(1))
        }
        cursor.close()
    }
    private fun addViewFlipper(){
        wordViewList = ArrayList()
        for(index in wordList.indices){
            val wordView = wordView(this)
            wordView.addWord(wordList[index])
            wordView.addDetail(detailList[index])
            wordView.showDetail()
            wordLearnView.addView(wordView)
            wordViewList.add(wordView)
        }
        size = wordViewList.size
    }
    private fun initListener(){
        wordLearnView.setOnTouchListener { v, event ->
            isMovingToLeft = event.x >= (v.width/2)
            false
        }
        wordLearnView.setOnClickListener {
            if(isMovingToLeft) {
                index++;
                if (index == size) {
                    index = 0;
                }
                wordLearnView.showNext()
            }
            else{
                index--;
                if(index < 0){
                    index = size-1
                }
                else{
                    wordLearnView.showPrevious()
                }
            }
            wordViewTextView.text = "정답률 : ${testCount[index]-failCount[index]}/${testCount[index]}"
        }
    }
}