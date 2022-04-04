package com.github.yeeun_yun97.toy.linksaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.MainActivity_textView)
        textView.setOnClickListener {
            startActivity(Intent(this,ViewLinkActivity::class.java))
        }
    }
}