package com.example.wordhelper

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1000)
        }

        var dbHelper = wordDBHelper(this)
        dbHelper.onCreate(dbHelper.writableDatabase)

        btnWordtest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test::class.java)
            startActivity(intent);
        }
        btnWordmanage.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_manage::class.java)
            startActivity(intent);
        }
    }
}