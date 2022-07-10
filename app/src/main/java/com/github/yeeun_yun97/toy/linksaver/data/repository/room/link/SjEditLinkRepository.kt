package com.github.yeeun_yun97.toy.linksaver.data.repository.room.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjEditLinkRepository @Inject constructor(
    private val dao: SjLinkDao
) {
    val linkName = MutableLiveData("")
    val linkIsVideo = MutableLiveData(false)
    private val _linkUrl = MutableLiveData("")
    private val _linkPreview = MutableLiveData("")
    val linkUrl: LiveData<String> = _linkUrl
    val linkPreview: LiveData<String> = _linkPreview

    private var targetLink: SjLink = SjLink(name = "", did = 1, url = "", type = ELinkType.link)
    private var targetDomain = SjDomain(did = 1, name = "", url = "")
    private val targetTags = mutableListOf<SjTag>()


    // manage liveData
    fun postLoadedLink(lid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val entity = dao.selectLinkByLid(lid)
            targetLink = entity.link
            updateAllLiveDataByLink()
            targetDomain = entity.domain
            targetTags.clear()
            targetTags.addAll(entity.tags)
        }

    fun postCreatedLink(url: String) =
        CoroutineScope(Dispatchers.IO).launch {
            targetLink = SjLink(lid = 0, did = 1, name = "", url = url, type = ELinkType.link)
            updateAllLiveDataByLink()
            targetDomain = SjDomain(did = 1, name = "", url = "")
            targetTags.clear()
        }

    private fun updateAllLiveDataByLink() {
        linkName.postValue(targetLink.name)
        linkIsVideo.postValue(targetLink.type == ELinkType.video)
        _linkPreview.postValue(targetLink.preview)
        _linkUrl.postValue(targetLink.url)
    }


    fun updatePreview(preview: String) {
        _linkPreview.postValue(preview)
        targetLink = targetLink.copy(preview = preview)
    }

    fun updateName(name: String) {
        linkName.postValue(name)
        targetLink = targetLink.copy(name = name)
    }



    // handle tag selection
    fun selectTag(tag: SjTag) = targetTags.add(tag)
    fun unselectTag(tag: SjTag) = targetTags.remove(tag)
    fun isTagSelected(tag: SjTag) = targetTags.contains(tag)


    fun saveLink() =
        CoroutineScope(Dispatchers.IO).launch {
            val type =
                when (linkIsVideo.value ?: false) {
                    true -> ELinkType.video
                    false -> ELinkType.link
                }
            targetLink = targetLink.copy(name = linkName.value ?: "", type = type)

            if (targetLink.lid == 0) {
                insertLinkAndTags(targetDomain, targetLink, targetTags).join()
            } else {
                updateLinkAndTags(targetDomain, targetLink, targetTags).join()
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
            launch { dao.deleteLinkTagCrossRefsByLid(link.lid) }.join()
            launch { insertLinkTagCrossRefs(link.lid, tags) }.join()
        }


}