package com.github.yeeun_yun97.toy.linksaver.application

import android.app.Application
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjCountDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDomainDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

const val RESULT_SUCCESS = 0
const val RESULT_FAILED = 1
const val RESULT_CANCELED = 2

@HiltAndroidApp
class LinkSaverApplication : Application() {

    @Inject
    lateinit var countDao : SjCountDao

    @Inject
    lateinit var domainDao : SjDomainDao

    @Inject
    lateinit var tagDao : SjTagDao

    override fun onCreate() {
        super.onCreate()
        // insert initial Data
        CoroutineScope(Dispatchers.IO).launch {
            val countDomain = async { countDao.getDomainCount() }
            if (countDomain.await() == 0) {
                domainDao.insertDomain(SjDomain(did = 1, name = "-", url = ""))
            }

            val countTagGroup = async { countDao.getTagGroupCount() }
            if (countTagGroup.await() == 0) {
                tagDao.insertTagGroup(SjTagGroup(gid = 1, name = "-", isPrivate = false))
            }
        }
    }
}