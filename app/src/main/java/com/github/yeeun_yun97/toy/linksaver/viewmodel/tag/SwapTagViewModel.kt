package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjViewTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwapTagViewModel @Inject constructor(
    private val tagRepo: SjViewTagGroupRepository
) : SjBaseViewModelImpl() {

    var gid: Int? = null
        set(data) {
            field = data
            refreshData()
        }

    val selectedBasicTags = mutableListOf<SjTag>()
    val selectedTargetTags = mutableListOf<SjTag>()

    //bindingVariable
    val bindingBasicTagGroup = tagRepo.defaultGroup
    val bindingTargetTagGroup =tagRepo.targetTagGroup

    override fun refreshData() {
        tagRepo.postDefaultTagGroup()
        tagRepo.postTargetTagGroup(gid!!)
    }

    // move tags and save
    fun moveSelectedTargetTagsToBasicGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepo.updateTagsToGid(selectedTargetTags, 1).join()
            refreshData()
            clearLists()
        }
    }

    fun moveSelectedBasicTagsToTargetGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepo.updateTagsToGid(selectedBasicTags, gid!!).join()
            refreshData()
            clearLists()
        }
    }

    private fun clearLists() {
        selectedBasicTags.clear()
        selectedTargetTags.clear()
    }


}