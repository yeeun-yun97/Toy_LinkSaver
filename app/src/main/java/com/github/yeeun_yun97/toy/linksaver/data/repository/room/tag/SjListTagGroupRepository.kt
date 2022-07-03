package com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjListTagGroupRepository @Inject constructor(
    private val dao: SjTagDao
) {
    private val _tagGroupsWithoutDefault = MutableLiveData<List<SjTagGroupWithTags>>()
    val tagGroupsWithoutDefault: LiveData<List<SjTagGroupWithTags>> get() = _tagGroupsWithoutDefault

    private val _defaultGroup = MutableLiveData<SjTagGroupWithTags>()
    val defaultGroup: LiveData<SjTagGroupWithTags> get() = _defaultGroup

    // manage liveData
    fun postTagGroupsNotDefault() =
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.selectTagGroupsNotDefault()
            _tagGroupsWithoutDefault.postValue(data)
        }

    fun postTagGroupsPublicNotDefault() =
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.selectTagGroupsPublicNotDefault()
            _tagGroupsWithoutDefault.postValue(data)
        }

    fun postDefaultTagGroup() =
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.selectDefaultTagGroup()
            _defaultGroup.postValue(data)
        }

    // insert
    suspend fun insertTagGroup(gid: Int = 0, name: String, isPrivate: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTagGroup(SjTagGroup(gid = gid, name = name, isPrivate = isPrivate))
        }

    fun insertTagToDefaultGroup(tid: Int = 0, name: String) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(SjTag(tid = tid, name = name, gid = 1))
        }

    //  update
    suspend fun updateTagGroup(tagGroup: SjTagGroup) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateTagGroup(tagGroup)
        }

    // delete
    fun deleteTagGroupByGid(gid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            // change tags related to group to basic group
            val job = launch { dao.updateTagsMoveToDefaultTagGroupByGid(gid) }

            job.join()
            // delete group
            dao.deleteTagGroupByGid(gid)
        }


}