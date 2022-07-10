package com.github.yeeun_yun97.toy.linksaver.viewmodel.link

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjEditLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjListTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditLinkViewModel @Inject constructor(
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

    // binding live data
    val bindingName: MutableLiveData<String> = editLinkRepo.linkName
    val bindingIsVideo: MutableLiveData<Boolean> = editLinkRepo.linkIsVideo
    val bindingUrl = editLinkRepo.linkUrl
    val bindingPreviewImage = editLinkRepo.linkPreview

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
            val titleJob = launch {
                val title = networkRepository.getTitleOf(url)
                editLinkRepo.updateName(title)
            }

            // load preview of url site
            val previewJob = launch {
                val preview = networkRepository.getPreviewOf(url)
                editLinkRepo.updatePreview(preview)
            }

            titleJob.join()
            previewJob.join()
        }
    }

    override fun refreshData() {
        if (isPrivateMode) {
            tagRepo.postTagGroupsNotDefaultPublic()
        } else {
            tagRepo.postTagGroupsNotDefault()
        }
        refreshDefaultGroup()
    }

    private fun refreshDefaultGroup() {
        tagRepo.postDefaultTagGroup()
    }

    fun createTag(name: String) =
        viewModelScope.launch(Dispatchers.IO) {
            tagRepo.insertTagToDefaultGroup(name = name).join()
            refreshDefaultGroup()
        }

    // handle tag selection
    fun selectTag(tag: SjTag) = editLinkRepo.selectTag(tag)
    fun unselectTag(tag: SjTag) = editLinkRepo.unselectTag(tag)
    fun isTagSelected(tag: SjTag) = editLinkRepo.isTagSelected(tag)

    // save link
    fun saveLink() {
        editLinkRepo.saveLink()
    }


}