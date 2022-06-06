package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagGroupEditViewModel : BasicViewModelWithRepository() {

    private var gid = -1

    private val _tagGroupWithTags = MutableLiveData<SjTagGroupWithTags>()
    val tagGroupWithTags: LiveData<SjTagGroupWithTags> get() = _tagGroupWithTags


    fun setGid(gid: Int) {
        this.gid = gid
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getTagGroupWithTagsByGid(gid)
            _tagGroupWithTags.postValue(result)
        }
    }

    // delete tag
    fun deleteTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteRefs = launch { repository.deleteTagRefs(tag) }
            deleteRefs.join()
            val deleteTag = launch { repository.deleteTag(tag) }
            deleteTag.join()
            setGid(gid)
        }
    }

    fun deleteTagGroup(gid: Int) = repository.deleteTagGroup(gid)

    fun editTag(tag: SjTag? = null, name: String, gid: Int = 1) {
        if (tag != null) {
            updateTag(tag.copy(name = name, gid = gid))
        } else {
            createTag(SjTag(name = name, gid = gid))
        }
    }

    private fun createTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { repository.insertTag(tag) }
            job.join()
            setGid(gid)
        }
    }

    private fun updateTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { repository.updateTag(tag) }
            job.join()
            setGid(gid)
        }
    }

}