package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

const val RESULT_SUCCESS = 0
const val RESULT_FAILED = 1
const val RESULT_CANCELED = 2

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

//        startKoin {
//            androidLogger()
//            androidContext(this@LinkSaverApplication)
//            fragmentFactory()
//            modules(vmModule, fragmentModule)
//        }
    }

    override fun onTerminate() {
        super.onTerminate()

        //XXX close Database
        // 사실 어디서 닫아야 할 지 잘 모르겠다.
        // 백그라운드로 갔을 때도 닫아야 할까?
        SjDatabaseUtil.closeDatabase()
    }


}