package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjViewTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagGroupEditViewModel @Inject constructor(
    private val repo: SjViewTagGroupRepository
) : SjBaseViewModelImpl() {

    var gid: Int = 1
        set(value) {
            field = value
            refreshData()
        }

    val tagGroup= repo.targetTagGroup
    val bindingTags: LiveData<List<SjTag>> = Transformations.map(tagGroup) { it.tags }

    override fun refreshData() {
        repo.postTargetTagGroup(gid!!)
    }

    // delete tag
    fun deleteTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteTag = repo.deleteTagByTid(tag.tid)
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
            repo.insertTag(name = name, gid = gid).join()
            refreshData()
        }
    }

    private fun updateTag(tag: SjTag) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch { repo.updateTag(tag) }
            job.join()
            refreshData()
        }
    }


}