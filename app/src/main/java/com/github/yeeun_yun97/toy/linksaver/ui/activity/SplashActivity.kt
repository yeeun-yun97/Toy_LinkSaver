package com.github.yeeun_yun97.toy.linksaver.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.yeeun_yun97.toy.linksaver.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    /*
    //TODO
    듣기로는 Splash를 Android12부터 지원하게 되었으며, 그것을 제대로 처리하지 못하면,
    Android12에서 앱을 실행할 때 splash가 두번 나올 수도 있다고 들었다.
    어떻게든 처리할 방법을 찾아볼 것.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        CoroutineScope(Dispatchers.Default).launch {
            val wait = launch { delay(1200) }
            wait.join()
            //wait and start Activity (And finish this activity)
            launch(Dispatchers.Main) {
                startMainActivityAndFinishThis()
            }
        }
    }

    // 메인액티비티를 시작하고, 이 액티비티를 종료한다.
    private fun startMainActivityAndFinishThis(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}