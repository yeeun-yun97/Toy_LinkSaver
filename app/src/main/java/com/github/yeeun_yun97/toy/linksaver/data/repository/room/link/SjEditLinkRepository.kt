package com.github.yeeun_yun97.toy.linksaver.data.repository.room.link

import android.util.Log
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
    val linkUrl = MutableLiveData("")
    val linkPreview = MutableLiveData("")

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

    private fun updateAllLiveDataByLink(){
        Log.d("Link name updated","by $targetLink")
        linkName.postValue(targetLink.name)
        linkIsVideo.postValue(targetLink.type == ELinkType.video)
        linkPreview.postValue(targetLink.preview)
        linkUrl.postValue(targetLink.url)
    }


    fun updatePreview(preview: String) {
        targetLink = targetLink.copy(preview = preview)
        linkPreview.postValue(preview)
    }

    fun updateName(name: String) {
        targetLink = targetLink.copy(name = name)
        Log.d("Link name updated", "to $name")
        linkName.postValue(name)
    }

    fun updateIsVideo(isVideo: Boolean) {
        val type =
            when (isVideo) {
                true -> ELinkType.video
                false -> ELinkType.link
            }
        targetLink = targetLink.copy(type = type)
        linkIsVideo.postValue(isVideo)
    }


    // handle tag selection
    fun selectTag(tag: SjTag) = targetTags.add(tag)
    fun unselectTag(tag: SjTag) = targetTags.remove(tag)
    fun isTagSelected(tag: SjTag) = targetTags.contains(tag)


    fun saveLink() =
        CoroutineScope(Dispatchers.IO).launch {
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