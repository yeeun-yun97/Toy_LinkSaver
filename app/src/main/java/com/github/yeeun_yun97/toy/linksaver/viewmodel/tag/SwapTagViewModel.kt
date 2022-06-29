package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwapTagViewModel @Inject constructor(
    private val tagRepo : SjTagRepository
) : SjBaseViewModelImpl() {
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
        refreshData()
    }

    override fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = tagRepo.selectTagGroupByGid(targetGid)
            _bindingTargetTagGroup.postValue(result)
        }
    }

    // move tags and save
    fun moveSelectedTargetTagsToBasicGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val updateJob = launch { tagRepo.updateTagsToGid(selectedTargetTags,1) }
            updateJob.join()
            refreshData()
            clearLists()
        }
    }

    fun moveSelectedBasicTagsToTargetGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val updateJob = launch { tagRepo.updateTagsToGid(selectedBasicTags,targetGid) }
            updateJob.join()
            refreshData()
            clearLists()
        }
    }

    private fun clearLists(){
        selectedBasicTags.clear()
        selectedTargetTags.clear()
    }




}