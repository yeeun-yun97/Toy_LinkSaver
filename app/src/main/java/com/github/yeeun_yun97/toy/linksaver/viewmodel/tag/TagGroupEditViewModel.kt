package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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
class TagGroupEditViewModel @Inject constructor(
    private val tagRepo: SjTagRepository
) : SjBaseViewModelImpl() {

    var gid: Int = 1
        set(value) {
            field = value
            refreshData()
        }

    private val _tagGroup = MutableLiveData<SjTagGroupWithTags>()
    val tagGroup: LiveData<SjTagGroupWithTags> get() = _tagGroup
    val bindingTags: LiveData<List<SjTag>> = Transformations.map(tagGroup) { it.tags }

    override fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = tagRepo.selectTagGroupByGid(gid)
            _tagGroup.postValue(result)
        }
    }

    // delete tag
    fun deleteTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteRefs = launch {
                tagRepo.deleteSearchTagCrossRefsByTid(tag.tid)
                tagRepo.deleteLinkTagCrossRefsByTid(tag.tid)
            }
            deleteRefs.join()
            val deleteTag = launch { tagRepo.deleteTag(tag.tid) }
            deleteTag.join()
            refreshData()
        }
    }

    fun editTag(tag: SjTag? = null, name: String) {
        if (tag != null) {
            updateTag(tag.copy(name = name, gid = gid))
        } else {
            createTag(name, gid)
        }
    }

    private fun createTag(name: String, gid: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { tagRepo.insertTag(name = name, gid = gid) }
            job.join()
            refreshData()
        }
    }

    private fun updateTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { tagRepo.updateTag(tag) }
            job.join()
            refreshData()
        }
    }


}