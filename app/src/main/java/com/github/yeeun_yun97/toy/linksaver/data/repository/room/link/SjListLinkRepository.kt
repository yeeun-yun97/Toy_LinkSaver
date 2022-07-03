package com.github.yeeun_yun97.toy.linksaver.data.repository.room.link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.model.*
import com.github.yeeun_yun97.toy.linksaver.ui.component.SjUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SjListLinkRepository @Inject constructor(
    private val dao: SjLinkDao
) {
    private val _links = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val links: LiveData<List<SjLinksAndDomainsWithTags>> get() = _links


    // manage liveData
    fun postLinksPublic() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectLinksPublic())
        }

    fun postAllLinks() =
        CoroutineScope(Dispatchers.IO).launch {
            _links.postValue(dao.selectAllLinks())
        }

    fun postLinksByKeywordAndTidsPublic(
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