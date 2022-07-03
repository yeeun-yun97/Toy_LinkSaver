package com.github.yeeun_yun97.toy.linksaver.data.repository.room.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjEditLinkRepository @Inject constructor(
    private val dao: SjLinkDao
) {
    private val _editLink = MutableLiveData<SjLink>()
    val editLink: LiveData<SjLink> get() = _editLink
    private val _loadedLinkData = MutableLiveData<SjLinksAndDomainsWithTags>()
    val loadedLinkData: LiveData<SjLinksAndDomainsWithTags> get() = _loadedLinkData

    // manage liveData
    fun postLoadedLink(lid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val entity = dao.selectLinkByLid(lid)
            _loadedLinkData.postValue(entity)
            _editLink.postValue(entity.link)
        }

    fun postCreatedLink(url: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val link = SjLink(lid = 0, did = 1, name = "", url = url, type = ELinkType.link)
            _editLink.postValue((link))
        }


    fun updatePreview(preview: String) {
        val link = editLink.value
        if (link is SjLink) {
            _editLink.postValue(editLink.value!!.copy(preview = preview))
        }
    }

    fun updateName(name: String) {
        val link = editLink.value
        if (link is SjLink) {
            _editLink.postValue(editLink.value!!.copy(name = name))
        }
    }

    fun updateIsVideo(isVideo: Boolean) {
        val link = editLink.value
        if (link is SjLink) {
            val type = when (isVideo) {
                true -> ELinkType.video
                false -> ELinkType.link
            }
            _editLink.postValue(editLink.value!!.copy(type = type))
        }
    }

    fun editLinkAndTags(targetDomain: SjDomain?, targetTags: List<SjTag>) =
        CoroutineScope(Dispatchers.IO).launch {
            val link = editLink.value!!
            if (link.lid == 0) {
                insertLinkAndTags(targetDomain, link, targetTags).join()
            } else {
                updateLinkAndTags(targetDomain, link, targetTags).join()
            }
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
    private fun updateLinkAndTags(domain: SjDomain?, link: SjLink, tags: List<SjTag>) =
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


}