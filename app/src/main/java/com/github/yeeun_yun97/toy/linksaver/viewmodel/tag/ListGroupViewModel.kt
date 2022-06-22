package com.github.yeeun_yun97.toy.linksaver.viewmodel.tag

import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjTagRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.SjBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListGroupViewModel : SjBaseViewModel() {
    private val tagRepo = SjTagRepository.getInstance()

    // data binding live data
    val bindingTagGroups = tagRepo.tagGroupsWithoutDefault
    val bindingBasicTagGroup = tagRepo.defaultTagGroup

    override fun refreshData() {
        when (isPrivateMode) {
            true -> tagRepo.postTagGroupsPublicNotDefault()
            false -> tagRepo.postTagGroupsNotDefault()
        }
    }

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) =
        CoroutineScope(Dispatchers.IO).launch {
            val job = launch {
                if (group == null) {
                    tagRepo.insertTagGroup(name, isPrivate)
                } else {
                    tagRepo.updateTagGroup(group.copy(name = name, isPrivate = isPrivate))
                }
            }
            job.join()
            refreshData()
        }

    fun deleteTagGroup(gid: Int) {
        tagRepo.deleteTagGroupByGid(gid)
    }


}