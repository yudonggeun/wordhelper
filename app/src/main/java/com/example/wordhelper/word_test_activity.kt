package com.example.wordhelper
//apache poi api를 사용하여 xls 파일을 읽는 기능
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_word_test_activity.*
import kotlinx.android.synthetic.main.activity_word_test_activity.view.*
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

class word_test_activity : AppCompatActivity() {
    lateinit var path : String
    lateinit var wordList : ArrayList<String>
    lateinit var detailList : ArrayList<ArrayList<String>>
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
        detailList = ArrayList<ArrayList<String>>()
        wordViewList = ArrayList<wordView>()
        path = filesDir.toString()+"/"
        var target : ArrayList<String>? = intent.getStringArrayListExtra("file")//"voca_bible_day1.xlsx"
        if(target == null || target!!.size == 0){
            finish()
        }
        if(target!!.size == 1){
            fileNameView.text = target!![0]
        }
        else{
            fileNameView.text = "다중 테스트"
        }
        for(fileName in target){
            addWord(fileName)
        }
        setTestList()
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
    private fun addWord(fileName : String){
        sqlDB = wordDBHelper.readableDatabase
        var cursor = sqlDB.rawQuery("SELECT word, detail FROM Word WHERE fileName is '$fileName';", null)
        while(cursor.moveToNext()){
            var details : ArrayList<String> = ArrayList()
            wordList.add(cursor.getString(0))
            details.add(cursor.getString(1))
            detailList.add(details)
        }
    }
    private fun setTestList(){//삭제 예정
        var test = arrayOf("apple", "banana", "graph", "mango", "orange")
        var test_detail = arrayOf(
            arrayOf("사과, 이런 저런 뜻이 있는데 이걸 알려주기 참 아쉽네", "뉴턴의 사과", "잘못된 뜻"),
            arrayOf("바나나"),
            arrayOf("포도"),
            arrayOf("망고"),
            arrayOf("오랜지")
        )
        for(a in test) wordList.add(a)
        for(a in test_detail){
            var index = test_detail.indexOf(a);
            detailList.add(ArrayList<String>())
            for(b in a){
                detailList.get(index).add(b)
            }
        }
    }
}