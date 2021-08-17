package com.example.wordhelper

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.SparseBooleanArray
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.util.size
import kotlinx.android.synthetic.main.activity_word_manage.*
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class word_manage : AppCompatActivity() {
    lateinit var dbHelper: wordDBHelper
    lateinit var wordDB : SQLiteDatabase
    lateinit var fileList : ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    lateinit var candidateFileList : ArrayList<String>

    var deleteMode : Boolean = false
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
        dbHelper.database = wordDB
        var cursor = wordDB!!.rawQuery("SELECT * FROM fileList", null);
        while(cursor.moveToNext()){
            fileList.add(cursor.getString(0))
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList)
        wordCorrectionList.adapter = adapter
    }
    private fun initBtnListener(){
        btnWordAdd.setOnClickListener {
            var downloadFileList : Array<out File> = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()).listFiles()
            candidateFileList = ArrayList()
            if(downloadFileList != null) {
                for (file in downloadFileList!!) {
                    if (file.isFile && file.name.substringAfter('.', "") == "xlsx") {
                        candidateFileList.add(file.name)
                    }
                }
            }

            var dialog = AlertDialog.Builder(this)
            var dialogAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1 ,candidateFileList)

            dialog.setTitle("Download")
            dialog.setPositiveButton("종료", null)
            dialog.setAdapter(dialogAdapter) { dialogInterface: DialogInterface, position: Int ->
                var path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString()
                var fileName = candidateFileList[position]
                if (fileName !in fileList) {
                    //다운로드 디렉토리에서 파일의 정보를 DB에 추가
                    dbHelper.addFileAtDB(fileName)
                    dbHelper.addWordListFrom(path, fileName)
                    //리스트뷰에 추가된 파일의 이름을 추가 후 갱신
                    fileList.add(fileName)
                    adapter.notifyDataSetChanged()
                }
            }
            dialog.show()
        }
        btnWordDelete.setOnClickListener {
            if(deleteMode) {
                var select : SparseBooleanArray = wordCorrectionList.checkedItemPositions
                for (index in select.size downTo 0) {
                    if (select[index]) {
                        wordDB.execSQL("DELETE FROM fileList WHERE fileName is '${fileList[index]}';")
                        wordDB.execSQL("DELETE FROM Word WHERE fileName is '${fileList[index]}';")
                        fileList.removeAt(index)
                    }
                }
                adapter.notifyDataSetChanged()
                deleteMode = !deleteMode
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList)
                wordCorrectionList.adapter = adapter
                wordCorrectionList.choiceMode = ListView.CHOICE_MODE_NONE
                wordCorrectionList.clearChoices()
            }
        }
        wordCorrectionList.setOnItemLongClickListener { parent, view, position, id ->
            deleteMode = !deleteMode
            if(deleteMode){
                adapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, fileList)
                wordCorrectionList.adapter = adapter
                wordCorrectionList.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            }
            else {
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