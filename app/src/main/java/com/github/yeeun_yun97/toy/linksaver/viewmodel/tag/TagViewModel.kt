package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class TagGroupDetailValue(
    val name : String,
    val isPrivate : Boolean,
    val tags : LiveData<List<SjTag>>
)

class TagViewModel : BasicViewModelWithRepository() {
    val tags = repository.tags
    val tagGroups = repository.tagGroups

    // data binding live data
    val bindingTagName = MutableLiveData<String>()

    // Model to save
    private var targetTag = SjTag(name = "")

    init {
        // handle user change data
        bindingTagName.observeForever {
            targetTag.name = it
            Log.d("TagName change", it)
        }
    }

    // set update tag data
    fun setTag(tid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tag = async { repository.getTagByTid(tid) }
            setTag(tag.await())
        }
    }

    private fun setTag(tag: SjTag) {
        targetTag = tag
        bindingTagName.postValue(tag.name)
    }


    // save tag
    fun saveTag() {
        if (targetTag.tid == 0) {
            repository.insertTag(targetTag)
        } else {
            repository.updateTag(targetTag)
        }
    }

    fun createTagGroup(name: String, isPrivate: Boolean){
        repository.insertTagGroup(name,isPrivate)
    }


    // delete tag
    fun deleteTag(tag: SjTag) = repository.deleteTag(tag)

}