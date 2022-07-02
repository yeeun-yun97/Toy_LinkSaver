package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjLinkListRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagListRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLinkViewModel @Inject constructor(
    private val tagRepo: SjTagListRepository,
    private val linkRepo: SjLinkListRepository,
    private val networkRepository: SjNetworkRepository,
) : SjUsePrivateModeViewModelImpl() {
    var lid: Int? = null
        set(value) {
            field = value
            refreshData()
        }

    // model list
    val tagGroups = tagRepo.tagGroupsWithoutDefault
    val tagDefaultGroup = tagRepo.defaultGroup

    // default type
    private val defaultType = ELinkType.link

    // Model to Save
    private var targetLink = SjLink(lid = 0, did = -1, name = "", url = "", type = defaultType)
    private var targetDomain = SjDomain(did = 1, name = "", url = "")
    private val targetTags = mutableListOf<SjTag>()

    // binding live data
    private val _previewImage = MutableLiveData<String>()
    private val _bindingUrl = MutableLiveData("")
    val bindingPreviewImage: LiveData<String> get() = _previewImage
    val bindingUrl: LiveData<String> get() = _bindingUrl
    val bindingName = MutableLiveData<String>()
    val bindingIsVideo = MutableLiveData(defaultType == ELinkType.video)


    init {
        bindingName.observeForever {
            targetLink = targetLink.copy(name = it)
        }

        bindingIsVideo.observeForever {
            targetLink = targetLink.copy(
                type = when (it) {
                    true -> ELinkType.video
                    false -> ELinkType.link
                }
            )
        }
    }

    override fun refreshData() {
        if (isPrivateMode) {
            tagRepo.postTagGroupsPublicNotDefault()
        } else {
            tagRepo.postTagGroupsNotDefault()
        }
        refreshDefaultGroup()
    }

    private fun refreshDefaultGroup() {
        tagRepo.postDefaultTagGroup()
    }

    // when create new with url address
    fun createLinkByUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            targetLink = targetLink.copy(url=url)
            _bindingUrl.postValue(url)

            // load title of url site
            launch {
                val title = networkRepository.getTitleOf(url)
                bindingName.postValue(title)
            }

            // load preview of url site
            launch {
                val preview = networkRepository.getPreviewOf(url)
                targetLink = targetLink.copy(preview=preview)
                _previewImage.postValue(preview)
            }
        }
    }

    // when updating existing SjLink
    fun setLinkByLid(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val link = async { linkRepo.selectLinkByLid(lid) }
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
        _bindingUrl.postValue(link.url)
        bindingName.postValue(link.name)
        _previewImage.postValue(link.preview)
        bindingIsVideo.postValue(link.type == ELinkType.video)
    }

    fun createTag(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepo.insertTag(name = name).join()
            refreshDefaultGroup()
        }
    }


    // handle tag selection
    fun selectTag(tag: SjTag) = targetTags.add(tag)
    fun unselectTag(tag: SjTag) = targetTags.remove(tag)
    fun isTagSelected(tag: SjTag) = targetTags.contains(tag)


    // save link
    fun saveVideo() {
        viewModelScope.launch {
            if (targetLink.lid != 0) {
                linkRepo.updateLinkAndTags(targetDomain, targetLink, targetTags).join()
            } else {
                linkRepo.insertLinkAndTags(targetDomain, targetLink, targetTags).join()
            }
        }

    }


}