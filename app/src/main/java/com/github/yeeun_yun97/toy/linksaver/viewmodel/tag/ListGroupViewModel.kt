package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjListTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListGroupViewModel @Inject constructor(
    private val tagListRepo: SjListTagGroupRepository
) : SjUsePrivateModeViewModelImpl() {
    // data binding live data
    val bindingTagGroups = tagListRepo.tagGroupsWithoutDefault
    val bindingBasicTagGroup = tagListRepo.defaultGroup

    override fun refreshData() {
        when (isPrivateMode) {
            true -> tagListRepo.postTagGroupsNotDefaultPublic()
            false -> tagListRepo.postTagGroupsNotDefault()
        }
        tagListRepo.postDefaultTagGroup()
    }

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) =
        CoroutineScope(Dispatchers.IO).launch {
            if (group == null) {
                tagListRepo.insertTagGroup(name = name, isPrivate = isPrivate).join()
            } else {
                tagListRepo.updateTagGroup(group.copy(name = name, isPrivate = isPrivate)).join()
            }
            refreshData()
        }

    fun deleteTagGroup(gid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            tagListRepo.deleteTagGroupByGid(gid).join()
            refreshData()
        }


}