package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import com.github.yeeun_yun97.toy.linksaver.data.dao.SjCountDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDomainDao
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjCountRepository @Inject
constructor(
    private val countDao: SjCountDao,
    private val domainDao: SjDomainDao,
    private val tagDao: SjTagDao,
) {

    fun insertDefaultDomainIfNotExists() =
        CoroutineScope(Dispatchers.IO).launch {
            if (getDomainCount() == 0) {
                domainDao.insertDomain(SjDomain(did = 1, name = "-", url = ""))
            }
        }

    fun insertDefaultGroupIfNotExists() =
        CoroutineScope(Dispatchers.IO).launch {
            if (getTagGroupCount() == 0) {
                tagDao.insertTagGroup(SjTagGroup(gid = 1, name = "-", isPrivate = false))
            }
        }

    suspend fun getLinkCount() =
        countDao.getLinkCount()

    suspend fun getTagGroupCount() =
        countDao.getTagGroupCount()

    suspend fun getDomainCount() =
        countDao.getDomainCount()

    suspend fun getTagCount() =
        countDao.getTagCount()

    suspend fun getSearchSetCount() =
            countDao.getSearchSetCount()

    suspend fun getLinkTagCrossRefCount() =
        countDao.getLinkTagCrossRefCount()

    suspend fun countAllSearchTagCrossRefs() =
        countDao.countAllSearchTagCrossRefs()


}