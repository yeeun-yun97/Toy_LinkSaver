package com.github.yeeun_yun97.toy.linksaver.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SjRepository private constructor() {
//
//    private val dao: SjDao = SjDatabaseUtil.getDao()
//
//    val domains: LiveData<List<SjDomain>> = dao.getAllDomains()
//
//    val linkList: LiveData<List<SjLinksAndDomainsWithTags>> = dao.getAllLinksAndDomainsWithTags()
//    val publicLinkList: LiveData<List<SjLinksAndDomainsWithTags>> =
//        dao.getPublicLinksAndDomainsWithTags()
//
//
//    val linkTypeLinkList = dao.getAllLinksByType(ELinkType.link.name)
//
    companion object {
        // singleton object
        private lateinit var repo: SjRepository

        fun getInstance(): SjRepository {
            if (!this::repo.isInitialized) {
                repo = SjRepository()
            }
            return repo
        }

    }

//
//
//    fun insertLinkAndTags(domain: SjDomain?, newLink: SjLink, tags: List<SjTag>) =
//        CoroutineScope(Dispatchers.IO).launch {
//            if (domain != null) newLink.did = domain.did
//            val lid = async { dao.insertLink(newLink).toInt() }
//
//            //insert crossRef after newLink insert
//            insertLinkTagCrossRefs(lid.await(), tags)
//        }
//
//    private suspend fun insertLinkTagCrossRefs(lid: Int, tags: List<SjTag>) {
//        val linkTagCrossRefs = mutableListOf<LinkTagCrossRef>()
//        for (tag in tags) {
//            linkTagCrossRefs.add(LinkTagCrossRef(lid = lid, tid = tag.tid))
//        }
//        dao.insertLinkTagCrossRefs(*linkTagCrossRefs.toTypedArray())
//    }
//
//

//
//

//

//
//

//

//
//

//

}