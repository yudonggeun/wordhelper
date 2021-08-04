package com.example.wordhelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_word_test_activity.*

class word_test_activity : AppCompatActivity() {
    lateinit var wordList : ArrayList<String>
    lateinit var detailList : ArrayList<List<String>>
    lateinit var wordViewList : ArrayList<wordView>
    lateinit var wordDBHelper: SQLiteOpenHelper
    lateinit var sqlDB : SQLiteDatabase
    var size : Int = 0
    var index : Int = 0
    var showDetail : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test_activity)
        wordDBHelper = wordDBHelper(this)
        wordList = ArrayList<String>()
        detailList = ArrayList<List<String>>()
        wordViewList = ArrayList<wordView>()
        var target : ArrayList<String>? = intent.getStringArrayListExtra("file")
        if(target == null || target!!.size == 0){
            finish()
        }
        if(target!!.size == 1){
            fileNameView.text = target!![0]
            if(target[0] == "weak"){
                fileNameView.text = "취약 테스트"
                var standard : Float = 0.5f
                addWord(weekCaseQuarry(standard))
            }
        }
        else{
            fileNameView.text = "다중 테스트"
        }
        for(fileName in target){
            addWord(normalCaseQuarry(fileName))
        }
        if(wordList.size == 0){
            Toast.makeText(this, "테스트할 단어가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        addViewFlipper()
        initClickListener()
    }
    private fun initClickListener(){
        btnShowdetail.setOnClickListener {
            var view : wordView = wordViewList[index]
            if(showDetail){
                view.showDetail()
                btnShowdetail.text = "뜻 감추기"
            }
            else{
                view.hideDetail()
                btnShowdetail.text = "뜻 보기"
            }
            showDetail = showDetail xor true
        }
        wordView.setOnClickListener {
            wordView.showNext()
            index = (index+1)%size
            progressView.text = (index+1).toString() + "/" + size
            var view : wordView = wordViewList[index]
            view.hideDetail()
            btnShowdetail.text = "뜻 보기"
            showDetail = true
        }
        progressView.text = (index+1).toString() + "/" + size
    }
    private fun addViewFlipper(){
        for(index in wordList.indices){
            val frameLayout = wordView(this)
            frameLayout.addWord(wordList[index])
            frameLayout.addDetail(detailList[index])
            wordView.addView(frameLayout)
            wordViewList.add(frameLayout)
            size++
        }
    }
    private fun weekCaseQuarry(standard : Float) : String{
        return "SELECT word, detail FROM Word WHERE failCount/testCount >= $standard;"
    }

    private fun normalCaseQuarry(fileName : String) : String{
        return "SELECT word, detail FROM Word WHERE fileName is '$fileName';"
    }
    private fun detailToArray(details : String) : List<String> {
        return details.split("^")
    }
    private fun addWord(quarry : String){
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery(quarry, null)
        while(cursor.moveToNext()){
            wordList.add(cursor.getString(0))
            detailList.add(detailToArray(cursor.getString(1)))
        }
    }
}