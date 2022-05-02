package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SjRepository private constructor() {

    private val dao: SjDao = SjDatabase.getDao()
    private var _searchLinkList = MutableLiveData<List<SjLinksAndDomainsWithTags>>()

    val searches: LiveData<List<SjSearchWithTags>> = dao.getAllSearch()
    val domains: LiveData<List<SjDomain>> = dao.getAllDomains()
    val tags: LiveData<List<SjTag>> = dao.getAllTags()
    val linkList: LiveData<List<SjLinksAndDomainsWithTags>> = dao.getAllLinksAndDomainsWithTags()
    val searchLinkList: LiveData<List<SjLinksAndDomainsWithTags>> get() = _searchLinkList

    companion object {
        private val repo: SjRepository = SjRepository()

        fun getInstance(): SjRepository = repo
    }

    fun searchLinksByLinkName(linkName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _searchLinkList.postValue(dao.searchLinksAndDomainsWithTagsByLinkName("%$linkName%"))
        }
    }

    fun searchLinksBySearchSet(keyword: String, selectedTags: MutableList<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            val list: MutableList<Int> = mutableListOf()
            for (tag in selectedTags) {
                list.add(tag.tid)
            }
            val result = dao.searchLinksAndDomainsWithTagsByLinkNameAndTags(
                "%$keyword%", list
            )
            Log.d("search result", result.toString())
            _searchLinkList.postValue(result)

        }
    }

    fun insertDomain(newDomain: SjDomain) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertDomain(newDomain)
        }

    fun insertTag(newTag: SjTag) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(newTag)
        }


    fun updateLink(domain: SjDomain, link: SjLink, tags: List<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            //update link
            link.did = domain.did
            dao.updateLink(link)

            //delete all crossRef
            dao.deleteLinkTagCrossRefsByLid(link.lid)

            //insert all crossRef
            insertLinkTagCrossRefs(link.lid, tags)
        }
    }

    fun insertLink(domain: SjDomain, newLink: SjLink, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            //set link domain
            newLink.did = domain.did

            //insert newLink
            val lid = async {
                insertLink(newLink)
            }

            //insert crossRef after newLink insert
            insertLinkTagCrossRefs(lid.await(), tags)
        }

    private suspend fun insertLink(newLink: SjLink): Int {
        return dao.insertLink(newLink).toInt()
    }

    private suspend fun insertLinkTagCrossRefs(lid: Int, tags: List<SjTag>) {
        val linkTagCrossRefs = mutableListOf<LinkTagCrossRef>()
        for (tag in tags) {
            linkTagCrossRefs.add(LinkTagCrossRef(lid = lid, tid = tag.tid))
            Log.d(javaClass.canonicalName, "added link cross ref lid = ${lid}, tid = ${tag.tid}")
        }
        dao.insertLinkTagCrossRefs(*linkTagCrossRefs.toTypedArray())
    }

    fun deleteDomain(domain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            val links = dao.getLinkAndDomainWithTagsByDid(domain.did)
            deleteLinks(links)
            dao.deleteDomain(domain)
        }
    }

    private fun deleteLinks(list: List<SjLinksAndDomainsWithTags>) {
        for (link in list) {
            deleteLink(link.link, link.tags)
        }
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {

            if (tags.isNotEmpty()) {
                val deleteRefs = launch {
                    val linkTagCrossRefs = mutableListOf<LinkTagCrossRef>()
                    for (tag in tags) {
                        linkTagCrossRefs.add(LinkTagCrossRef(lid = link.lid, tid = tag.tid))
                    }
                    dao.deleteLinkTagCrossRefs(*linkTagCrossRefs.toTypedArray())
                }
                deleteRefs.join()
            }
            dao.deleteLink(link)

        }

    fun deleteSearch() {
        CoroutineScope(Dispatchers.IO).launch {
            val job = launch { dao.deleteAllSearchTagCrossRefs() }
            job.join()
            dao.deleteAllSearch()
        }
    }

    fun deleteTag(tag: SjTag) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteTag(tag)
            dao.deleteLinkTagCrossRefsByTid(tag.tid)
            //링크와 태그 크로스 레프 객체에서 참조하고 있을 수 있으니,
            //마찬가지로 확인하고, 있으면 지우지 말고 알리기
        }
    }

    fun saveSearchAndTags(newSearch: SjSearch, selectedTags: MutableList<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            val sid = async { insertSearch(newSearch) }
            insertSearchTagCrossRef(sid.await(), selectedTags)
        }
    }

    fun updateTag(tag: SjTag) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateTag(tag)
        }
    }

    fun updateDomain(domain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateDomain(domain)
        }
    }

    private suspend fun insertSearch(newSearch: SjSearch): Int {
        return dao.insertSearch(newSearch).toInt()
    }

    private suspend fun insertSearchTagCrossRef(sid: Int, tags: MutableList<SjTag>) {
        val searchTagCrossRefs = mutableListOf<SearchTagCrossRef>()
        for (tag in tags) {
            searchTagCrossRefs.add(SearchTagCrossRef(sid = sid, tid = tag.tid))
            Log.d(javaClass.canonicalName, "added link cross ref sid = ${sid}, tid = ${tag.tid}")
        }
        dao.insertSearchTagCrossRefs(*searchTagCrossRefs.toTypedArray())
    }

    suspend fun getLinkAndDomainWithTagsByLid(lid: Int): SjLinksAndDomainsWithTags {
        return dao.getLinkAndDomainWithTagsByLid(lid)
    }

    suspend fun getTagByTid(tid: Int): SjTag {
        return dao.getTagByTid(tid)
    }

    suspend fun getDomainByDid(did: Int): SjDomain {
        return dao.getDomainByDid(did)
    }


}