package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import kotlinx.coroutines.*

class LinkSaverApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        //open Database
        SjDatabaseUtil.openDatabase(applicationContext)

        //insert initial Data
        CoroutineScope(Dispatchers.IO).launch {

            val countDomain = async {
                SjDatabaseUtil.getDao().getDomainCount()
            }
            if (countDomain.await() == 0) {
                SjDatabaseUtil.getDao().insertDomain(SjDomain(did = 1, name = "-", url = ""))
            }

            val countTagGroup = async {
                SjDatabaseUtil.getDao().getTagGroupCount()
            }
            if (countTagGroup.await() == 0) {
                SjDatabaseUtil.getDao().insertTagGroup(SjTagGroup(gid = 1, name = "-", isPrivate = false))
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        //close Database
        //사실 어디서 닫아야 할 지 잘 모르겠다.
        //백그라운드로 갔을 때도 닫아야 할까?
        SjDatabaseUtil.closeDatabase()
    }


}