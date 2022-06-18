package com.github.yeeun_yun97.toy.linksaver.data.repository

import com.github.yeeun_yun97.toy.linksaver.data.dao.SjLinkDao
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabaseUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag

// refactor SjRepository and move use this
class SjRoomRepository private constructor() {
    private val dao: SjLinkDao = SjDatabaseUtil.getLinkDao()

    companion object {
        // singleton object
        private lateinit var repo: SjRoomRepository

        fun getInstance(): SjRoomRepository {
            if (!this::repo.isInitialized) {
                repo = SjRoomRepository()
            }
            return repo
        }
    }

    suspend fun selectLinksPublic() = dao.selectLinksPublic()

    suspend fun selectAllLinks() = dao.selectAllLinks()

    suspend fun selectLinksByNameAndTagsPublic(
        keyword: String,
        tags: List<SjTag>
    ): List<SjLinksAndDomainsWithTags> {
        return when (tags.isEmpty()) {
            true -> {

                dao.selectLinksByNamePublic(keyword)
            }
            false -> {
                val tids = mutableListOf<Int>().apply {
                    for (tag in tags) add(tag.tid)
                }
                dao.selectLinksByNameAndTagsPublic("%$keyword%", tids, tids.size)
            }
        }
    }

    suspend fun selectLinksByNameAndTags(
        keyword: String,
        tags: List<SjTag>
    ): List<SjLinksAndDomainsWithTags> {
        return when (tags.isEmpty()) {
            true -> {
                val result = dao.selectLinksByName("%$keyword%")
                return result
            }
            false -> {
                val tids = mutableListOf<Int>().apply {
                    for (tag in tags) add(tag.tid)
                }
                dao.selectLinksByNameAndTags(keyword, tids, tids.size)
            }
        }

    }


}