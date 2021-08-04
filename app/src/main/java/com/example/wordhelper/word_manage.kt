package com.example.wordhelper

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.util.size
import kotlinx.android.synthetic.main.activity_word_manage.*
import kotlinx.android.synthetic.main.activity_word_test.*
import java.io.File

class word_manage : AppCompatActivity() {
    lateinit var dbHelper: SQLiteOpenHelper
    lateinit var wordDB : SQLiteDatabase
    lateinit var fileList : ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    var deleteMode : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_manage)

        dbHelper = wordDBHelper(this)
        addFileList()
        initBtnListener()

    }

    private fun FileList(){
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
    private fun addFileList(){
        fileList = ArrayList<String>()
        wordDB = dbHelper.readableDatabase
        var cursor = wordDB!!.rawQuery("SELECT * FROM fileList", null);
        while(cursor.moveToNext()){
            fileList.add(cursor.getString(0))
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList)
        wordCorrectionList.adapter = adapter
    }
    fun initBtnListener(){
        btnWordAdd.setOnClickListener {

        }
        btnWordDelete.setOnClickListener {
            if(deleteMode) {
                var select: SparseBooleanArray = wordCorrectionList.checkedItemPositions
                for (index in select.size downTo 0) {
                    if (select[index]) {
                        wordDB.execSQL("DELETE FROM fileList WHERE fileName is '${fileList[index]}';")
                        wordDB.execSQL("DELETE FROM Word WHERE fileName is '${fileList[index]}';")
                        fileList.removeAt(index)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
        wordCorrectionList.setOnItemLongClickListener { parent, view, position, id ->
            deleteMode = !deleteMode
            if(deleteMode){
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, fileList)
                wordCorrectionList.adapter = adapter
                wordCorrectionList.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            }
            else{
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList)
                wordCorrectionList.adapter = adapter
                wordCorrectionList.choiceMode = ListView.CHOICE_MODE_NONE
            }
            true
        }
        wordCorrectionList.setOnItemClickListener { parent, view, position, id ->
            if(!deleteMode) {
                var intent: Intent = Intent(applicationContext, word_view::class.java)
                intent.putExtra("fileName", fileList[position])
                startActivity(intent)
            }
        }
    }

}