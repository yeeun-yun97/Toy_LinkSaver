package com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjTagDao
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjViewTagGroupRepository @Inject constructor(
    private val dao: SjTagDao
) {
    private val _targetTagGroup = MutableLiveData<SjTagGroupWithTags>()
    val targetTagGroup: LiveData<SjTagGroupWithTags> get() = _targetTagGroup

    private val _defaultGroup = MutableLiveData<SjTagGroupWithTags>()
    val defaultGroup: LiveData<SjTagGroupWithTags> get() = _defaultGroup


    fun postTargetTagGroup(gid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.selectTagGroupByGid(gid)
            _targetTagGroup.postValue(data)
        }

    fun postDefaultTagGroup() =
        CoroutineScope(Dispatchers.IO).launch {
            val data = dao.selectDefaultTagGroup()
            _defaultGroup.postValue(data)
        }

    suspend fun updateTagsToGid(tags: List<SjTag>, gid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            val tagList = mutableListOf<SjTag>()
            for (tag in tags) tagList.add(tag.copy(gid=gid))
            dao.updateTags(*tagList.toTypedArray())
        }

    suspend fun updateTag(tag: SjTag) {
        dao.updateTag(tag)
    }

    fun insertTag(tid: Int = 0, name: String, gid: Int = 1) =
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertTag(SjTag(tid = tid, name = name, gid = gid))
        }

    suspend fun deleteTagByTid(tid: Int) =
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                deleteSearchTagCrossRefsByTid(tid)
                deleteLinkTagCrossRefsByTid(tid)
            }.join()
            launch {
                dao.deleteTagByTid(tid)
            }.join()
        }

    private suspend fun deleteLinkTagCrossRefsByTid(tid: Int) =
        dao.deleteLinkTagCrossRefsByTid(tid)

    private suspend fun deleteSearchTagCrossRefsByTid(tid: Int) =
        dao.deleteSearchTagCrossRefsByTid(tid)


}