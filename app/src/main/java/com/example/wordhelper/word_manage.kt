package com.example.wordhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_word_manage.*

class word_manage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_manage)
        btnEditandview.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_view::class.java)
            startActivity(intent)
        }
    }
}