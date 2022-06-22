package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SwapTagViewModel : ViewModel() {
    private val tagRepo = SjTagRepository.getInstance()

    var targetGid: Int = -1
    val selectedBasicTags = mutableListOf<SjTag>()
    val selectedTargetTags = mutableListOf<SjTag>()

    //bindingVariable
    private val _bindingBasicTagGroup = tagRepo.defaultTagGroup
    private val _bindingTargetTagGroup = MutableLiveData<SjTagGroupWithTags>()
    val bindingBasicTagGroup: LiveData<SjTagGroupWithTags> get() = _bindingBasicTagGroup
    val bindingTargetTagGroup: LiveData<SjTagGroupWithTags> get() = _bindingTargetTagGroup

    fun setTargetTagGroupByGid(gid: Int) {
        this.targetGid = gid
        loadTargetTagGroup()
    }

    private fun loadTargetTagGroup() {
        viewModelScope.launch(Dispatchers.IO) {
//            val result = repository.getTagGroupWithTagsByGid(targetGid)
//            _bindingTargetTagGroup.postValue(result)
        }
    }


    // move tags and save
    fun moveSelectedTargetTagsToBasicGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            for (tag in selectedTargetTags) {
                tag.gid = 1 // basicGid
            }
//            val updateJob = launch { repository.updateTags(selectedTargetTags) }
//            updateJob.join()
            loadTargetTagGroup()
            clearLists()
        }
    }

    fun moveSelectedBasicTagsToTargetGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            for (tag in selectedBasicTags) {
                tag.gid = targetGid
            }
//            val updateJob = launch { repository.updateTags(selectedBasicTags) }
//            updateJob.join()
            loadTargetTagGroup()
            clearLists()
        }
    }

    private fun clearLists(){
        selectedBasicTags.clear()
        selectedTargetTags.clear()
    }


}