package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjSearchSetDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SearchTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjSearchSetRepository @Inject constructor(
    private val dao: SjSearchSetDao
) {
    private val _searchSetList = MutableLiveData<List<SjSearchWithTags>>()
    val searchSetList: LiveData<List<SjSearchWithTags>> get() = _searchSetList

    // manage liveData
    fun postSearchSetPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _searchSetList.postValue(dao.selectSearchSetsPublic())
        }

    fun postAllSearchSet() =
        CoroutineScope(Dispatchers.IO).launch {
            _searchSetList.postValue(dao.selectAllSearchSets())
        }

    // insert
    fun insertSearchSet(sid: Int = 0, keyword: String, tids: List<Int>) =
        CoroutineScope(Dispatchers.IO).launch {
            // if there is conflict, delete
            val deleteConflictJob = launch {
                val conflictedSearchSetSids =
                    if (tids.isEmpty()) dao.selectSearchSetByKeyword(keyword)
                    else dao.selectSearchSetByKeywordAndTags(keyword, tids)
                dao.deleteSearchTagCrossRefsBySids(conflictedSearchSetSids)
                dao.deleteSearchSetBySids(conflictedSearchSetSids)
            }

            // insert new searchSet
            val newSearchSetSid = async {
                deleteConflictJob.join()
                dao.insertSearchSet(SjSearch(sid = sid, keyword = keyword)).toInt()
            }

            // insert TagCrossRef after insert
            val insertTagCrossRefJob = launch {
                val sid = newSearchSetSid.await()
                val searchTagCrossRefs = mutableListOf<SearchTagCrossRef>()
                for (tid in tids) {
                    searchTagCrossRefs.add(SearchTagCrossRef(sid = sid, tid = tid))
                }
                dao.insertSearchTagCrossRefs(*searchTagCrossRefs.toTypedArray())
            }
            insertTagCrossRefJob.join()
        }


    // delete
    fun deleteAllSearchSet() =
        CoroutineScope(Dispatchers.IO).launch {
            // delete all refs
            val deleteSearchTagCrossRefJob = launch { dao.deleteAllSearchTagCrossRefs() }

            //  delete after delete
            val deleteSearchSetJob = launch {
                deleteSearchTagCrossRefJob.join()
                dao.deleteAllSearchSets()
            }
            deleteSearchSetJob.join()
        }
}