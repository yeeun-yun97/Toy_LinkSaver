package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagGroupViewModel : BasicViewModelWithRepository() {

    var gid: Int = -1
    val selectedBasicTags = mutableListOf<SjTag>()
    val selectedTargetTags = mutableListOf<SjTag>()

    //bindingVariable
    private val _bindingBasicTagGroup = repository.basicTagGroup
    private val _bindingTargetTagGroup = MutableLiveData<SjTagGroupWithTags>()
    val bindingBasicTagGroup: LiveData<SjTagGroupWithTags> get() = _bindingBasicTagGroup
    val bindingTargetTagGroup: LiveData<SjTagGroupWithTags> get() = _bindingTargetTagGroup


    private fun loadTargetTagGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getTagGroupWithTagsByGid(gid)
            _bindingTargetTagGroup.postValue(result)
        }
    }

    fun setTargetTagGroupGid(gid:Int){
        this.gid = gid
        loadTargetTagGroup()
    }

    fun moveSelectedTargetTagsToBasicGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            for (tag in selectedTargetTags) {
                tag.gid = bindingBasicTagGroup.value!!.tagGroup.gid
            }
            val updateJob = launch{repository.updateTags(selectedTargetTags)}
            updateJob.join()
            loadTargetTagGroup()
        }
    }

    fun moveSelectedBasicTagsToTargetGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            for (tag in selectedBasicTags) {
                tag.gid = bindingTargetTagGroup.value!!.tagGroup.gid
            }
            val updateJob = launch{repository.updateTags(selectedBasicTags)}
            updateJob.join()
            loadTargetTagGroup()
        }
    }

}