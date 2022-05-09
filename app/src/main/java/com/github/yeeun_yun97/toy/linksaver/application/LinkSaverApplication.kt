package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import kotlinx.coroutines.*

class LinkSaverApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //open Database
        SjDatabase.openDatabase(applicationContext)

        //insert initial Data
        CoroutineScope(Dispatchers.IO).launch {

            val count = async {
                SjDatabase.getDao().getDomainCount()
            }
            if (count.await() == 0) {
                SjDatabase.getDao().insertDomain(SjDomain(did = 1, name = "-", url = ""))
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        //close Database
        //사실 어디서 닫아야 할 지 잘 모르겠다.
        //백그라운드로 갔을 때도 닫아야 할까?
        SjDatabase.closeDatabase()
    }
}