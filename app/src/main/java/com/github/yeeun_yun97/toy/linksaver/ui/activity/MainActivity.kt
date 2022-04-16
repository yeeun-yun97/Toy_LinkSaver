package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.yeeun_yun97.toy.linksaver.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.MainActivity_textView)
        textView.setOnClickListener {
            startActivity(Intent(this, ViewLinkActivity::class.java))
        }
    }
}