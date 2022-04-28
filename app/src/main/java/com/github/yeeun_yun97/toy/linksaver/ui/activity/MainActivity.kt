package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.yeeun_yun97.toy.linksaver.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.Default).launch{
            val wait = launch{ delay(1200)}
            wait.join()
            launch(Dispatchers.Main){
                val intent : Intent = Intent(applicationContext,ViewLinkActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}