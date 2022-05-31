package com.github.yeeun_yun97.toy.linksaver.viewmodel.edit_link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditVideoViewModel : BasicViewModelWithRepository() {
    // repo
    private val networkRepository = SjNetworkRepository.newInstance()

    // model list
    val tagList = repository.tags

    // default type
    private val defaultType = ELinkType.link

    // Model to Save
    private var targetLink = SjLink(lid = 0, did = -1, name = "", url = "", type = defaultType)
    private var targetDomain = SjDomain(did = 1, name = "", url = "")
    private val targetTags = mutableListOf<SjTag>()

    // binding live data
    private val _previewImage = MutableLiveData<String>()
    val bindingPreviewImage: LiveData<String> get() = _previewImage
    val bindingName = MutableLiveData<String>()
    val bindingIsVideo = MutableLiveData(defaultType == ELinkType.video)
    val bindingUrl get() = targetDomain.url + targetLink.url


    init {
        bindingName.observeForever {
            targetLink.name = it
        }

        bindingIsVideo.observeForever {
            targetLink.type =
                when (it) {
                    true -> ELinkType.video
                    false -> ELinkType.link
                }
        }
    }

    // when create new with url address
    fun createLinkByUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            targetLink.url = url

            // load title of url site
            launch {
                val title = networkRepository.getTitleOf(url)
                bindingName.postValue(title)
            }

            // load preview of url site
            launch {
                val preview = networkRepository.getPreviewOf(url)
                targetLink.preview = preview
                _previewImage.postValue(preview)
            }

            // 아이콘 불러오기
        }
    }

    // when updating existing SjLink
    fun setLinkByLid(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val link = async { repository.getLinkAndDomainWithTagsByLid(lid) }
            setData(link.await())
        }
    }

    private fun setData(link: SjLinksAndDomainsWithTags) {
        setLink(link.link)
        setDomain(link.domain)
        setTags(link.tags)
    }

    private fun setTags(tags: List<SjTag>) {
        targetTags.clear()
        targetTags.addAll(tags)
    }

    private fun setDomain(domain: SjDomain) {
        targetDomain = domain
    }

    private fun setLink(link: SjLink) {
        targetLink = link
        bindingName.postValue(link.name)
        _previewImage.postValue(link.preview)
        bindingIsVideo.postValue(link.type == ELinkType.video)
    }


    // handle tag selection
    fun selectTag(tag: SjTag) = targetTags.add(tag)
    fun unselectTag(tag: SjTag) = targetTags.remove(tag)
    fun isTagSelected(tag: SjTag) = targetTags.contains(tag)


    // save link
    fun saveVideo() {
        if (targetLink.lid != 0) {
            repository.updateLinkAndTags(targetDomain, targetLink, targetTags)
        } else {
            repository.insertLinkAndTags(targetDomain, targetLink, targetTags)
        }
    }


}