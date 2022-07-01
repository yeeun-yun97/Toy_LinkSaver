package com.github.yeeun_yun97.toy.linksaver.data.repository.room

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
class SjTagRepository @Inject constructor(
    private val dao: SjTagDao
) {
    private val _tagGroupsWithoutDefault = MutableLiveData<List<SjTagGroupWithTags>>()
    val tagGroupsWithoutDefault: LiveData<List<SjTagGroupWithTags>> get() = _tagGroupsWithoutDefault
    val defaultTagGroup = dao.getDefaultTagGroupData()

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


    // select
    suspend fun selectTagByTid(tid: Int): SjTag = dao.selectTagByTid(tid)

    suspend fun selectTagGroupByGid(gid: Int): SjTagGroupWithTags =
        dao.selectTagGroupByGid(gid)


    // insert
    suspend fun insertTagGroup(gid :Int = 0, name: String, isPrivate: Boolean) =
        dao.insertTagGroup(SjTagGroup(gid = gid, name = name, isPrivate = isPrivate))


    private fun insertTag(newTag: SjTag) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(newTag)
        }

    fun insertTag(tid :Int = 0, name: String, gid: Int = 1) = insertTag(SjTag(tid = tid, name = name, gid = gid))

    //  update
    suspend fun updateTag(tag: SjTag) {
        dao.updateTag(tag)
    }

    suspend fun updateTagsToGid(tags: List<SjTag>, gid: Int) {
        for (tag in tags) tag.gid = gid

        dao.updateTags(*tags.toTypedArray())

    }

    suspend fun updateTagGroup(tagGroup: SjTagGroup) {
        dao.updateTagGroup(tagGroup)
    }


    // delete
    fun deleteTagGroupByGid(gid: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // change tags related to group to basic group
            val job = launch { dao.updateTagsMoveToDefaultTagGroupByGid(gid) }

            job.join()
            // delete group
            dao.deleteTagGroupByGid(gid)
        }
    }

    suspend fun deleteLinkTagCrossRefsByTid(tid: Int) {
        dao.deleteLinkTagCrossRefsByTid(tid)
    }

    suspend fun deleteSearchTagCrossRefsByTid(tid: Int) {
        dao.deleteSearchTagCrossRefsByTid(tid)
    }

    suspend fun deleteTag(tid: Int) {
        // wait and delete
        dao.deleteTagByTid(tid)
    }


}