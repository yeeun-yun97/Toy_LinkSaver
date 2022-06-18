package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjSearchSetDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SearchTagCrossRef
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SjSearchSetRepository private constructor() {

    private val dao: SjSearchSetDao = SjDatabaseUtil.getDatabase().getSearchSetDao()

    private val _searchSetList = MutableLiveData<List<SjSearchWithTags>>()
    val searchSetList: LiveData<List<SjSearchWithTags>> get() = _searchSetList

    companion object {
        // singleton object
        private lateinit var repo: SjSearchSetRepository

        fun getInstance(): SjSearchSetRepository {
            if (!this::repo.isInitialized) {
                repo = SjSearchSetRepository()
            }
            return repo
        }
    }

    suspend fun loadSearchSetPublic() {

        _searchSetList.postValue()
    }

    suspend fun loadAllSearchSet() {

        _searchSetList.postValue()
    }

    suspend fun deleteAllSearchSet() =
        CoroutineScope(Dispatchers.IO).launch {
            //delete all refs
            val job = launch { dao.deleteAllSearchTagCrossRefs() }
            job.join()
            // wait and delete
            dao.deleteAllSearch()
        }

    suspend fun insertSearchSet(keyword: String, tags: List<SjTag>) {
        CoroutineScope(Dispatchers.IO).launch {
            //find if there is already same searchData and delete them
            val sids = async {
                getSearchBySearchWordAndTags(searchWord, selectedTags)
            }
            deleteSearchesBySids(sids.await())

            //insert new searchData
            val newSid = async {
                sids.await()
                dao.insertSearch(SjSearch(keyword = searchWord)).toInt()
            }
            insertSearchTagCrossRef(newSid.await(), selectedTags)
        }
    }

    private suspend fun getSearchBySearchWordAndTags(
        searchWord: String,
        selectedTags: List<SjTag>
    ): List<Int> {
        return if (selectedTags.isEmpty()) {
            dao.getSearchWithTagsBySearchWord(searchWord)
        } else {
            val tids = mutableListOf<Int>()
            for (tag in selectedTags) {
                tids.add(tag.tid)
            }
            dao.getSearchWithTagsBySearchWordAndTags(searchWord, tids)
        }
    }

    private suspend fun deleteSearchesBySids(sids: List<Int>) {
        dao.deleteSearchTagCrossRefsBySid(sids)
        dao.deleteSearches(sids)
    }

    private suspend fun insertSearchTagCrossRef(sid: Int, tags: List<SjTag>) {
        val searchTagCrossRefs = mutableListOf<SearchTagCrossRef>()
        for (tag in tags) {
            searchTagCrossRefs.add(SearchTagCrossRef(sid = sid, tid = tag.tid))
        }
        dao.insertSearchTagCrossRefs(*searchTagCrossRefs.toTypedArray())
    }


}