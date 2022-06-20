package com.github.yeeun_yun97.toy.linksaver.data.repository.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SjLinkRepository private constructor() {
    private val dao: SjLinkDao = SjDatabaseUtil.getLinkDao()

    private val _links = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val links: LiveData<List<SjLinksAndDomainsWithTags>> get() = _links

    companion object {
        // singleton object
        private lateinit var repo: SjLinkRepository

        fun getInstance(): SjLinkRepository {
            if (!this::repo.isInitialized) {
                repo = SjLinkRepository()
            }
            return repo
        }
    }

    // manage liveData
    fun postLinksPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectLinksPublic())
        }

    fun postAllLinks() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectAllLinks())
        }

    fun postLinksPublicByKeywordAndTids(
        keyword: String,
        tids: List<Int>
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val links = when (tids.isEmpty()) {
                true -> dao.selectLinksByNamePublic("%$keyword%")
                false -> dao.selectLinksByNameAndTagsPublic("%$keyword%", tids, tids.size)
            }
            _links.postValue(links)
        }

    fun postLinksByKeywordAndTids(
        keyword: String,
        tids: List<Int>
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val links = when (tids.isEmpty()) {
                true -> dao.selectLinksByName("%$keyword%")
                false -> dao.selectLinksByNameAndTags("%$keyword%", tids, tids.size)
            }
            _links.postValue(links)
        }


}