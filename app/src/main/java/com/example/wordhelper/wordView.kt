package com.example.wordhelper

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get

class wordView (context : Context, attr: AttributeSet? = null) : LinearLayout(context, attr){
    init{
        initializeStyledArributes(attr)
    }
    private fun initializeStyledArributes(attrs : AttributeSet?){
        this.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        this.orientation = LinearLayout.VERTICAL
    }
    public fun addWord(word : String){
        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 4f
        layoutParams.gravity = Gravity.CENTER
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER
        textView.text = word
        textView.textSize = 70f
        this.addView(textView)
    }

    public fun addDetail(details: List<String>){
        var text : String = ""
        for(index in details.indices){
            text += "${index+1}. ${details[index]}\n"
        }
        val textView = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 6f
        layoutParams.gravity = Gravity.CENTER
        textView.visibility = View.GONE
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER
        textView.textSize = 30f
        textView.text = text;
        this.addView(textView)
    }
    public fun showDetail(){
        this[1].visibility = View.VISIBLE
    }
    public fun hideDetail(){
        this[1].visibility = View.GONE
    }
}