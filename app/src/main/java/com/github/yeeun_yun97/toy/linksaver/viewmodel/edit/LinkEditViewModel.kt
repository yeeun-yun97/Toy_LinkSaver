package com.github.yeeun_yun97.toy.linksaver.viewmodel.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjEditLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjListTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkEditViewModel @Inject constructor(
    private val tagRepo: SjListTagGroupRepository,
    private val editLinkRepo: SjEditLinkRepository,
    private val networkRepository: SjNetworkRepository,
) : SjUsePrivateModeViewModelImpl() {
    var lid: Int? = null
        set(value) {
            field = value
            setLinkByLid(lid!!)
            refreshData()
        }

    var url: String? = null
        set(value) {
            field = value
            createLinkByUrl(url!!)
            refreshData()
        }

    // model list
    val tagGroups = tagRepo.tagGroupsWithoutDefault
    val tagDefaultGroup = tagRepo.defaultGroup
    private val loadedLinkData = editLinkRepo.loadedLinkData

    // Model to Save
    private val targetLink = editLinkRepo.editLink
    private var targetDomain = SjDomain(did = 1, name = "", url = "")
    private val targetTags = mutableListOf<SjTag>()

    // binding live data
    val bindingPreviewImage: LiveData<String> = Transformations.map(targetLink) { it.preview }
    val bindingUrl: LiveData<String> get() = Transformations.map(targetLink) { it.url }
    val bindingName = MutableLiveData("")
    val bindingIsVideo = MutableLiveData<Boolean>()


    init {
        // user edit
        bindingName.observeForever { editLinkRepo.updateName(it) }
        bindingIsVideo.observeForever { editLinkRepo.updateIsVideo(it) }

        // loaded link data
        loadedLinkData.observeForever {
            targetDomain = it.domain

            targetTags.clear()
            targetTags.addAll(it.tags)
        }
        targetLink.observeForever {
            bindingName.postValue(it.name)
            bindingIsVideo.postValue(it.type == ELinkType.video)
        }
    }

    private fun setLinkByLid(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            editLinkRepo.postLoadedLink(lid).join()
        }
    }

    // when create new with url address
    private fun createLinkByUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            editLinkRepo.postCreatedLink(url).join()

            // load title of url site
            launch {
                val title = networkRepository.getTitleOf(url)
                editLinkRepo.updateName(title)
            }.join()

            // load preview of url site
            launch {
                val preview = networkRepository.getPreviewOf(url)
                editLinkRepo.updatePreview(preview)
            }.join()
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

    fun createTag(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepo.insertTagToDefaultGroup(name = name).join()
            refreshDefaultGroup()
        }
    }

    // handle tag selection
    fun selectTag(tag: SjTag) = targetTags.add(tag)
    fun unselectTag(tag: SjTag) = targetTags.remove(tag)
    fun isTagSelected(tag: SjTag) = targetTags.contains(tag)

    // save link
    fun saveLink() {
        editLinkRepo.editLinkAndTags(targetDomain, targetTags)
    }


}