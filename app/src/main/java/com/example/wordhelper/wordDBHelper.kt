package com.example.wordhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class wordDBHelper(context: Context) : SQLiteOpenHelper(context, "Word", null, 1) {
    lateinit var database: SQLiteDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        database = db!!
        db!!.execSQL("Create table IF NOT EXISTS fileList(fileName CHAR(20) Primary key);")
        db!!.execSQL("Create table IF NOT EXISTS Word(fileName CHAR(20), word CHAR(20), detail CHAR(150), testCount INTEGER, failCount INTEGER, turn INTEGER );")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS fileList")
    }
    public fun addFileAtDB(fileName : String){
        database.execSQL("INSERT INTO fileList VALUES('${fileName}')")
    }
    //액셀에서 한 단어의 뜻을 기제할 때 한 칸에 동일한(비슷한) 뜻을 적는다.
    public fun addWordListFrom(path : String, fileName: String){
        var formatter = DataFormatter()
        var file = File("$path/$fileName")
        var workbook = XSSFWorkbook(file)
        for(sheet in workbook){
            for(row in sheet){
                var word : String = ""
                var detail : String = ""
                var turn : Int = 0

                for(cel in row){
                    var cellRef = CellReference(row.rowNum, cel.columnIndex)
                    if(cel.columnIndex == 0){
                        word = formatter.formatCellValue(cel)
                        continue
                    }
                    detail = formatter.formatCellValue(cel)
                    turn = cel.columnIndex

                    database.execSQL("INSERT INTO Word VALUES('$fileName','$word', '$detail', 0, 0, $turn)")
                }
            }
        }
    }
}