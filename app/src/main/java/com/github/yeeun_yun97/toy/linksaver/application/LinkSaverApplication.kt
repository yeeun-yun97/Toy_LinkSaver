package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import androidx.room.Room
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LinkSaverApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db =
            Room.databaseBuilder(applicationContext, SjDatabase::class.java, "sj_database").build()
        SjDatabase.db = db
        GlobalScope.launch(Dispatchers.IO) {
            //SjDatabase.insertFirstData()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}