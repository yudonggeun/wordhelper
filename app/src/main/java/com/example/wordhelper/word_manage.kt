package com.example.wordhelper

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.core.util.size
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.synthetic.main.activity_word_manage.*
import java.io.*
import java.util.logging.Logger

class word_manage : AppCompatActivity() {
    lateinit var dbHelper: wordDBHelper
    lateinit var wordDB : SQLiteDatabase
    lateinit var fileList : ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    private val OPEN_FILE_REQUEST_CODE = 1000
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
           val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
                val uri = Uri.parse(Environment.getExternalStorageDirectory().path+"/Download/")
                setDataAndType(uri, "application/*")
                addCategory(Intent.CATEGORY_OPENABLE)
            }

            startActivityForResult(intent, OPEN_FILE_REQUEST_CODE)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var uri : Uri? = null
            if(data != null){
                uri = data!!.data
                Log.i(TAG , "Uri : " + uri.toString())

                readExcelFileFromUri(uri!!)
            }
        }
    }
    private fun readExcelFileFromUri(uri: Uri){
        var fileName : String = ""
        //파일이름 읽기
        uri.let {
            contentResolver.query(uri, null, null, null, null)
        }?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            fileName = it.getString(nameIndex)
        }
        contentResolver.openInputStream(uri)?.use { inputStream ->
            //다운로드 디렉토리에서 파일의 정보를 DB에 추가
            dbHelper.addFileAtDB(fileName)
            dbHelper.addWordListFrom(inputStream, fileName)
            //리스트뷰에 추가된 파일의 이름을 추가 후 갱신
            fileList.add(fileName)
            adapter.notifyDataSetChanged()
        }
    }
}