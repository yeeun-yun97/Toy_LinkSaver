package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class ListGroupViewModel : BasicViewModelWithRepository() {
    val tagGroups = repository.tagGroups
    val publicTagGroups = repository.publicTagGroups

    // data binding live data
    private val _bindingBasicTagGroup = repository.defaultTagGroup
    val bindingBasicTagGroup: LiveData<SjTagGroupWithTags> get() = _bindingBasicTagGroup

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) {
        repository.editTagGroup(name, isPrivate, group)
    }

    fun deleteTagGroup(gid:Int){
        repository.deleteTagGroup(gid)
    }


}