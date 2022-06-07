package com.github.yeeun_yun97.toy.linksaver.data.repository

import android.util.Log
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

    private val dao: SjDao = SjDatabaseUtil.getDao()

    private val _searchLinkList = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val searchLinkList: LiveData<List<SjLinksAndDomainsWithTags>> get() = _searchLinkList
    val searches: LiveData<List<SjSearchWithTags>> = dao.getAllSearch()
    val domains: LiveData<List<SjDomain>> = dao.getAllDomains()
    val domainsExceptDefault: LiveData<List<SjDomain>> = dao.getAllDomainsExceptDefault()
    val tags: LiveData<List<SjTag>> = dao.getAllTags()
    val tagGroups: LiveData<List<SjTagGroupWithTags>> = dao.getTagGroupsWithTags()
    val tagGroupsWithDefault: LiveData<List<SjTagGroupWithTags>> = dao.getAllTagGroupsWithTags()
    val defaultTagGroup: LiveData<SjTagGroupWithTags> = dao.getBasicTagGroupWithTags()
    val linkList: LiveData<List<SjLinksAndDomainsWithTags>> = dao.getAllLinksAndDomainsWithTags()

    val linkTypeVideoList = dao.getAllLinksByType(ELinkType.video.name)
    val linkTypeLinkList = dao.getAllLinksByType(ELinkType.link.name)

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


    // insert methods
    fun insertDomain(newDomain: SjDomain) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertDomain(newDomain)
        }

    fun insertTag(newTag: SjTag) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(newTag)
        }

    fun insertLinkAndTags(domain: SjDomain?, newLink: SjLink, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            if (domain != null) newLink.did = domain.did
            val lid = async { dao.insertLink(newLink).toInt() }

            //insert crossRef after newLink insert
            insertLinkTagCrossRefs(lid.await(), tags)
        }

    private suspend fun insertLinkTagCrossRefs(lid: Int, tags: List<SjTag>) {
        val linkTagCrossRefs = mutableListOf<LinkTagCrossRef>()
        for (tag in tags) {
            linkTagCrossRefs.add(LinkTagCrossRef(lid = lid, tid = tag.tid))
        }
        dao.insertLinkTagCrossRefs(*linkTagCrossRefs.toTypedArray())
    }

    fun insertSearchAndTags(searchWord: String, selectedTags: List<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            //find if there is already same searchData and delete them
            val sids = async {
                getSearchBySearchWordAndTags(searchWord, selectedTags)
            }
            deleteSearchesBySids(sids.await())

            //insert new searchData
            val newSid = async {
                sids.await()
                dao.insertSearch(SjSearch(keyword = searchWord)).toInt()
            }
            insertSearchTagCrossRef(newSid.await(), selectedTags)
        }
    }

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (group == null) {
                insertTagGroup(SjTagGroup(name = name, isPrivate = isPrivate))
            } else {
                updateTagGroup(group.copy(name = name, isPrivate = isPrivate))
            }
        }
    }

    suspend fun updateTagGroup(tagGroup: SjTagGroup) {
        dao.updateTagGroup(tagGroup)
    }

    suspend fun insertTagGroup(tagGroup: SjTagGroup) {
        dao.insertTagGroup(tagGroup)
    }

    private suspend fun deleteSearchesBySids(sids: List<Int>) {
        dao.deleteSearchTagCrossRefsBySid(sids)
        dao.deleteSearches(sids)
    }

    private suspend fun getSearchBySearchWordAndTags(
        searchWord: String,
        selectedTags: List<SjTag>
    ): List<Int> {
        return if (selectedTags.isEmpty()) {
            dao.getSearchWithTagsBySearchWord(searchWord)
        } else {
            val tids = mutableListOf<Int>()
            for (tag in selectedTags) {
                tids.add(tag.tid)
            }
            dao.getSearchWithTagsBySearchWordAndTags(searchWord, tids)
        }
    }

    private suspend fun insertSearchTagCrossRef(sid: Int, tags: List<SjTag>) {
        val searchTagCrossRefs = mutableListOf<SearchTagCrossRef>()
        for (tag in tags) {
            searchTagCrossRefs.add(SearchTagCrossRef(sid = sid, tid = tag.tid))
        }
        dao.insertSearchTagCrossRefs(*searchTagCrossRefs.toTypedArray())
    }


    // search methods
    fun searchLinksBySearchSet(keyword: String, selectedTags: List<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            if (selectedTags.isEmpty()) {
                searchByLinkName(keyword)
            } else {
                searchByLinkNameAndTags(keyword, selectedTags)
            }
        }
    }

    private suspend fun searchByLinkName(keyword: String) {
        val result = dao.searchLinksAndDomainsWithTagsByLinkName(
            "%$keyword%"
        )
        _searchLinkList.postValue(result)
        Log.d("repository search", "with keyword")
        Log.d("repository search", "result $result")
    }

    private suspend fun searchByLinkNameAndTags(keyword: String, selectedTags: List<SjTag>) {
        val list: MutableList<Int> = mutableListOf()
        for (tag in selectedTags) {
            list.add(tag.tid)
        }
        val result = dao.searchLinksAndDomainsWithTagsByLinkNameAndTags(
            "%$keyword%", list, list.size
        )
        _searchLinkList.postValue(result)
        Log.d("repository search", "with keyword and tags")
        Log.d("repository search", "result $result")
    }


    // update methods
    fun updateLinkAndTags(domain: SjDomain?, link: SjLink, tags: MutableList<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            if (domain != null) link.did = domain.did
            dao.updateLink(link)

            //update tags:: delete all and insert all
            dao.deleteLinkTagCrossRefsByLid(link.lid)
            insertLinkTagCrossRefs(link.lid, tags)
        }
    }

    suspend fun updateTag(tag: SjTag) {
        dao.updateTag(tag)
    }

    fun updateDomain(domain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateDomain(domain)
        }
    }


    // delete methods
    fun deleteDomain(domain: SjDomain) {
        CoroutineScope(Dispatchers.IO).launch {
            //delete all related links
            val url = domain.url
            val updateLinks = mutableListOf<SjLink>()
            val job = launch {
                val links = dao.getLinkAndDomainWithTagsByDid(domain.did)
                for (link in links) {
                    link.link.url = StringBuilder(url).append(link.link.url).toString()
                    link.link.did = 1
                    updateLinks.add(link.link)
                }
                dao.updateLinks(*updateLinks.toTypedArray())
            }
            job.join()
            //wait and delete
            dao.deleteDomain(domain)
        }
    }

    private fun deleteLinks(list: List<SjLinksAndDomainsWithTags>) {
        for (link in list) {
            //TODO 이거 마음에 안드는데, 더 좋은 방법 생각해볼 것.
            deleteLink(link.link, link.tags)
        }
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            if (tags.isNotEmpty()) {
                //delete all related tag refs
                val deleteRefs = launch {
                    val linkTagCrossRefs = mutableListOf<LinkTagCrossRef>()
                    for (tag in tags) {
                        linkTagCrossRefs.add(LinkTagCrossRef(lid = link.lid, tid = tag.tid))
                    }
                    dao.deleteLinkTagCrossRefs(*linkTagCrossRefs.toTypedArray())
                }
                deleteRefs.join()
            }
            //wait and delete
            dao.deleteLink(link)
        }

    fun deleteLinkByLid(lid: Int, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            if (tags.isNotEmpty()) {
                //delete all related tag refs
                val deleteRefs = launch {
                    val tids = mutableListOf<Int>()
                    for (tag in tags) {
                        tids.add(tag.tid)
                    }
                    dao.deleteLinkTagCrossRefsByLidAndTid(lid, tids)
                }
                deleteRefs.join()
            }
            //wait and delete
            dao.deleteLinkByLid(lid)
        }

    fun deleteSearch() {
        CoroutineScope(Dispatchers.IO).launch {
            //delete all refs
            val job = launch { dao.deleteAllSearchTagCrossRefs() }
            job.join()
            // wait and delete
            dao.deleteAllSearch()
        }
    }

    suspend fun deleteTagRefs(tag: SjTag) {
        //delete all refs
        dao.deleteLinkTagCrossRefsByTid(tag.tid)
    }

    suspend fun deleteTag(tag: SjTag) {
        // wait and delete
        dao.deleteTag(tag)
    }

    fun deleteTagGroup(gid: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // change tags related to group to basic group
            val job = launch { dao.updateTagToBasicGroupByGid(gid) }

            job.join()
            // delete group
            dao.deleteTagGroupByGid(gid)
        }
    }


    // get query by key
    suspend fun getLinkAndDomainWithTagsByLid(lid: Int): SjLinksAndDomainsWithTags =
        dao.getLinkAndDomainWithTagsByLid(lid)

    suspend fun getLinkDetailDataByLid(lid: Int): LinkDetailValue {
        val entity = dao.getLinkAndDomainWithTagsByLid(lid)
        val vo = LinkDetailValue(
            lid = entity.link.lid,
            name = entity.link.name,
            fullUrl = LinkModelUtil.getFullUrl(entity),
            preview = entity.link.preview,
            isVideo = when (entity.link.type) {
                ELinkType.video -> true
                ELinkType.link -> false
            },
            isYoutubeVideo =
            entity.link.type == ELinkType.video &&
                    SjUtil.checkYoutubePrefix(
                        LinkModelUtil.getFullUrl(entity)
                    ),
            tags = entity.tags
        )
        return vo
    }


    suspend fun getTagByTid(tid: Int): SjTag = dao.getTagByTid(tid)

    suspend fun getDomainByDid(did: Int): SjDomain = dao.getDomainByDid(did)

    suspend fun getTagGroupWithTagsByGid(gid: Int): SjTagGroupWithTags =
        dao.getTagGroupWithTagsByGid(gid)

    suspend fun updateTags(tags: List<SjTag>) {
        dao.updateTags(*tags.toTypedArray())
    }


}