package com.example.wordhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_word_test.*

class word_test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test)
        var csvName : ArrayList<String> = ArrayList()

        btnRandomtest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            intent.putStringArrayListExtra("files", csvName)
            startActivity(intent);
        }
        btnWeektest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            intent.putStringArrayListExtra("files", csvName)
            startActivity(intent);
        }
        btnMultitest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            intent.putStringArrayListExtra("files", csvName)
            startActivity(intent);
        }
    }
}