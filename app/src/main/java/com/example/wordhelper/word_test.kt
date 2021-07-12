package com.example.wordhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_word_test.*

class word_test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_test)
        btnRandomtest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            startActivity(intent);
        }
        btnWeektest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            startActivity(intent);
        }
        btnMultitest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test_activity::class.java)
            startActivity(intent);
        }
    }
}