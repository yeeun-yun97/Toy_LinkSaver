package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SjLinkRepository private constructor() {
    private val dao: SjLinkDao = SjDatabaseUtil.getLinkDao()

    private val _links = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val links: LiveData<List<SjLinksAndDomainsWithTags>> get() = _links

    companion object {
        // singleton object
        private lateinit var repo: SjLinkRepository

        fun getInstance(): SjLinkRepository {
            if (!this::repo.isInitialized) {
                repo = SjLinkRepository()
            }
            return repo
        }
    }

    // manage liveData
    fun postLinksPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectLinksPublic())
        }

    fun postAllLinks() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectAllLinks())
        }

    fun postLinksPublicByKeywordAndTids(
        keyword: String,
        tids: List<Int>
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val links = when (tids.isEmpty()) {
                true -> dao.selectLinksByNamePublic("%$keyword%")
                false -> dao.selectLinksByNameAndTagsPublic("%$keyword%", tids, tids.size)
            }
            _links.postValue(links)
        }

    fun postLinksByKeywordAndTids(
        keyword: String,
        tids: List<Int>
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val links = when (tids.isEmpty()) {
                true -> dao.selectLinksByName("%$keyword%")
                false -> dao.selectLinksByNameAndTags("%$keyword%", tids, tids.size)
            }
            _links.postValue(links)
        }


    // select single
    suspend fun selectLinkByLid(lid: Int): SjLinksAndDomainsWithTags =
        dao.selectLinkByLid(lid)

    suspend fun selectLinkValueByLid(lid: Int): LinkDetailValue {
        val entity = dao.selectLinkByLid(lid)
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
                        entity.link.url
                    ),
            tags = entity.tags
        )
        return vo
    }


    // insert
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


    // update
    fun updateLinkAndTags(domain: SjDomain?, link: SjLink, tags: MutableList<SjTag>) {
        Log.d("링크 업데이트", tags.toString())
        CoroutineScope(Dispatchers.IO).launch {
            if (domain != null) link.did = domain.did
            dao.updateLink(link)

            //update tags:: delete all and insert all
            val deleteJob = launch { dao.deleteLinkTagCrossRefsByLid(link.lid) }
            val insertJob = launch {
                deleteJob.join()
                insertLinkTagCrossRefs(link.lid, tags)
            }
            insertJob.join()
        }
    }


    // delete
    fun deleteLinkByLid(lid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            //delete all related tag refs
            val deleteRefs = launch {
                dao.deleteLinkTagCrossRefsByLid(lid)
            }
            deleteRefs.join()
            //wait and delete
            dao.deleteLinkByLid(lid)
        }


}