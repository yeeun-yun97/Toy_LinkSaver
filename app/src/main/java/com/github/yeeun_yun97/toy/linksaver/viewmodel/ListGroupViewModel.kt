package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListGroupViewModel @Inject constructor(
    private val tagRepo: SjTagRepository
) : SjUsePrivateModeViewModelImpl() {
    // data binding live data
    val bindingTagGroups = tagRepo.tagGroupsWithoutDefault
    val bindingBasicTagGroup = tagRepo.defaultGroup

    override fun refreshData() {
        when (isPrivateMode) {
            true -> tagRepo.postTagGroupsPublicNotDefault()
            false -> tagRepo.postTagGroupsNotDefault()
        }
        tagRepo.postDefaultTagGroup()
    }

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) =
        CoroutineScope(Dispatchers.IO).launch {
            val job = launch {
                if (group == null) {
                    tagRepo.insertTagGroup(name = name, isPrivate = isPrivate).join()
                } else {
                    tagRepo.updateTagGroup(group.copy(name = name, isPrivate = isPrivate)).join()
                }
            }
            job.join()
            refreshData()
        }

    fun deleteTagGroup(gid: Int) {
        tagRepo.deleteTagGroupByGid(gid)
    }


}