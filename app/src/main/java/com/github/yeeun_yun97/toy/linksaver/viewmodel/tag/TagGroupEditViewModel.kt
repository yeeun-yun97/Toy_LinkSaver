package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.*
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagGroupEditViewModel : ViewModel() {
    private val tagRepo = SjTagRepository.getInstance()

    var gid: Int = 1
        set(value) {
            field = value
            loadTagGroup()
        }

    private val _tagGroup = MutableLiveData<SjTagGroupWithTags>()
    val tagGroup: LiveData<SjTagGroupWithTags> get() = _tagGroup
    val bindingTags: LiveData<List<SjTag>> = Transformations.map(tagGroup) { it.tags }

    fun loadTagGroup() {
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
            loadTagGroup()
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
            val job = launch { tagRepo.insertTag(name, gid) }
            job.join()
            loadTagGroup()
        }
    }

    private fun updateTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { tagRepo.updateTag(tag) }
            job.join()
            loadTagGroup()
        }
    }


}