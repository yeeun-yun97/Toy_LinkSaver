package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjLinkRepository @Inject constructor(
    private val dao: SjLinkDao
) {
    private val _links = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val links: LiveData<List<SjLinksAndDomainsWithTags>> get() = _links


    // manage liveData
    fun postLinksPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectLinksPublic())
        }

    fun postAllLinks() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectAllLinks())
        }

    fun postLinksByKeywordAndTidsPublic(
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
            val updatedLink = newLink.copy(did = domain?.did ?: 1)
            val lid = async { dao.insertLink(updatedLink).toInt() }

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
    fun updateLinkAndTags(domain: SjDomain?, link: SjLink, tags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            val updatedLink = if (domain != null) link.copy(did = domain.did) else link
            dao.updateLink(updatedLink)

            //update tags:: delete all and insert all
            val deleteJob = launch { dao.deleteLinkTagCrossRefsByLid(link.lid) }
            val insertJob = launch {
                deleteJob.join()
                insertLinkTagCrossRefs(link.lid, tags)
            }
            insertJob.join()
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