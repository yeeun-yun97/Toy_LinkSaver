package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroup
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjTagRepository private constructor() {
    private val dao: SjTagDao = SjDatabaseUtil.getTagDao()

    private val _tagGroupsWithoutDefault = MutableLiveData<List<SjTagGroupWithTags>>()
    val tagGroupsWithoutDefault: LiveData<List<SjTagGroupWithTags>> get() = _tagGroupsWithoutDefault

    val defaultTagGroup = dao.getDefaultTagGroupData()

    companion object {
        // singleton object
        private lateinit var repo: SjTagRepository

        fun getInstance(): SjTagRepository {
            if (!this::repo.isInitialized) {
                repo = SjTagRepository()
            }
            return repo
        }
    }


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
    private suspend fun insertTagGroup(tagGroup: SjTagGroup) {
        dao.insertTagGroup(tagGroup)
    }

    private fun insertTag(newTag: SjTag) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(newTag)
        }

    fun insertTag(name:String)=insertTag(SjTag(name=name))

    //  update
    suspend fun updateTags(tags: List<SjTag>) {
//        dao.updateTags(*tags.toTypedArray())
    }

    suspend fun updateTag(tag: SjTag) {
        dao.updateTag(tag)
    }

    fun editTagGroup(name: String, isPrivate: Boolean, group: SjTagGroup?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (group == null) {
                insertTagGroup(SjTagGroup(name = name, isPrivate = isPrivate))
            } else {
                updateTagGroup(group.copy(name = name, isPrivate = isPrivate))
            }
        }
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

    suspend fun deleteLinkTagCrossRefByTid(tid: Int) {
        //delete all refs
        dao.deleteLinkTagCrossRefsByTid(tid)
    }

    suspend fun deleteTag(tid: Int) {
        // wait and delete
        dao.deleteTagByTid(tid)
    }


}