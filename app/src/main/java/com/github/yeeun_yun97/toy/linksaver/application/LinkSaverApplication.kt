package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.fragmentModule
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.vmModule
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

@HiltAndroidApp
class LinkSaverApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // open Database
        SjDatabaseUtil.openDatabase(applicationContext)

        // insert initial Data
        CoroutineScope(Dispatchers.IO).launch {
            val dao = SjDatabaseUtil.getCountDao()

            val countDomain = async {
                dao.getDomainCount()
            }
            if (countDomain.await() == 0) {
                SjDatabaseUtil.getDomainDao().insertDomain(SjDomain(did = 1, name = "-", url = ""))
            }

            val countTagGroup = async {
                dao.getTagGroupCount()
            }
            if (countTagGroup.await() == 0) {
                SjDatabaseUtil.getTagDao()
                    .insertTagGroup(SjTagGroup(gid = 1, name = "-", isPrivate = false))
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@LinkSaverApplication)
            fragmentFactory()
            modules(vmModule, fragmentModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        //XXX close Database
        // 사실 어디서 닫아야 할 지 잘 모르겠다.
        // 백그라운드로 갔을 때도 닫아야 할까?
        SjDatabaseUtil.closeDatabase()
    }


}