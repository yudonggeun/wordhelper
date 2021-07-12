package com.example.wordhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnWordtest.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_test::class.java)
            startActivity(intent);
        }
        btnWordmanage.setOnClickListener {
            var intent : Intent = Intent(applicationContext, word_manage::class.java)
            startActivity(intent);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}