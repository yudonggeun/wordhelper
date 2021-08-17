package com.example.wordhelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_word_test_activity.*
import kotlinx.android.synthetic.main.activity_word_view.*

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
        wordTestViewFlipper.setOnClickListener {
            wordTestViewFlipper.showNext()
            index++;
            if(index == size) {
                Toast.makeText(this, "테스트를 완료하였습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            progressView.text = (index%size+1).toString() + "/$size"
            var view : wordView = wordViewList[index%size]
            view.hideDetail()
            btnShowdetail.text = "뜻 보기"
            showDetail = true
        }
        progressView.text = "1/$size"
    }
    private fun addViewFlipper(){
        wordViewList = ArrayList()
        for(index in wordList.indices){
            val wordView = wordView(this)
            wordView.addWord(wordList[index])
            wordView.addDetail(detailList[index])
            wordTestViewFlipper.addView(wordView)
            wordViewList.add(wordView)
        }
        size = wordViewList.size
    }
    private fun weekCaseQuarry(standard : Float) : String{
        return "SELECT word, detail, turn FROM Word WHERE failCount/testCount >= $standard order by word, turn;"
    }

    private fun normalCaseQuarry(fileName : String) : String{
        return "SELECT word, detail, turn FROM Word WHERE fileName is '$fileName' order by word, turn;"
    }
    private fun addWord(quarry : String) {
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery(quarry, null)
        var detail: ArrayList<String>? = null
        while (cursor.moveToNext()) {
            if (cursor.getInt(2) == 1) {
                detail = ArrayList()
                wordList.add(cursor.getString(0))
                detailList.add(detail)
            }
            detail!!.add(cursor.getString(1))
        }
        cursor.close()
    }
}