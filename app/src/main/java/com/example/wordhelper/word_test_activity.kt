package com.example.wordhelper
//apache poi api를 사용하여 xls 파일을 읽는 기능
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
    var size : Int = 0
    var index : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test_activity)
        wordList = ArrayList<String>()
        detailList = ArrayList<ArrayList<String>>()
        wordViewList = ArrayList<wordView>()
        path = filesDir.toString()+"/"
        fileNameView.text = "voca_bible_day1.xlsx"
        var target : String ="voca_bible_day1.xlsx"
        //readExcelFileFromAssets(path + target)
        setTestList()
        addViewFlipper()
        initClickListener()

        progressView.text = (index+1).toString() + "/" + size
    }
    data class SearchData(val word : String, val detail : String)
    private fun initClickListener(){
        btnShowdetail.setOnClickListener {
            var view : wordView = wordViewList[index]
            view.switchVisible()
        }
        wordView.setOnClickListener {
            wordView.showNext()
            index = (index+1)%size
            progressView.text = (index+1).toString() + "/" + size
        }
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
    private fun setTestList(){
        var test = arrayOf("apple", "banana", "graph", "mango", "orange")
        var test_detail = arrayOf(
            arrayOf("사과", "뉴턴의 사과", "잘못된 뜻"),
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
    private fun readExcelFileFromAssets(path : String) {
        val pkg = OPCPackage.open(File(path))
        val wb : Workbook = XSSFWorkbook(pkg)
        var sheet : Sheet = wb.getSheetAt(0)
        for(row in sheet){
            detailList.add(ArrayList<String>())
            for(cell in row){
                var cellRef : CellReference = CellReference(row.rowNum, cell.columnIndex)

                when(cell.columnIndex){
                    0 ->{//word
                        if(cell.cellType == CellType.STRING){
                            wordList.add(cell.stringCellValue)
                        }
                    }
                    1->{//detail
                        var detail  = detailList.get(row.rowNum)
                        if(cell.cellType == CellType.STRING){
                            detail.add(cell.stringCellValue)
                        }
                    }
                }
            }
        }
        pkg.close()
    }
}