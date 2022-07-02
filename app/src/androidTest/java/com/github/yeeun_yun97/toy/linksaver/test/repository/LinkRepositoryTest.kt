package com.github.yeeun_yun97.toy.linksaver.test.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class LinkRepositoryTest : SjBaseTest() {
    private lateinit var links: LiveData<List<SjLinksAndDomainsWithTags>>

    override fun before() {
        links = linkRepo.links
    }

    private val keyword = "검색"
    private val tags = listOf(
        SjTestDataUtil.testTags[0],
        SjTestDataUtil.testTags[3]
    )
    private val tids: List<Int>
        get() {
            val tidList = mutableListOf<Int>()
            for (tag in tags) tidList.add(tag.tid)
            return tidList
        }

    private fun postAllLinks() =
        linkRepo.postAllLinks()

    private fun postLinksPublic() =
        linkRepo.postLinksPublic()

    private fun postLinksByKeywordAndTidsPublic() =
        linkRepo.postLinksByKeywordAndTidsPublic(keyword, tids)

    private fun postLinksByKeywordAndTids() =
        linkRepo.postLinksByKeywordAndTids(keyword, tids)

    @Test
    fun listAllLinks() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val result = getValueOrThrow(links, ::postAllLinks)
            Assert.assertEquals(SjTestDataUtil.testLinks.size, result.size)
        }
    }

    @Test
    fun listLinksPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val resultPublic = getValueOrThrow(links, ::postLinksPublic)
            Assert.assertEquals(SjTestDataUtil.testLinksPublic.size, resultPublic.size)
        }
    }

    @Test
    fun listSearchedLinksWithKeywordAndTids() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinks) {
                if (keyword in link.first.name && link.second.containsAll(tags))
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTids)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun listSearchedLinksWithTidsPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinksPublic) {
                if (link.second.containsAll(tags))
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTidsPublic)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun listSearchedLinksWithKeywordPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinksPublic) {
                if (keyword in link.first.name)
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTidsPublic)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun listSearchedLinksWithKeywordAndTidsPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinksPublic) {
                if (keyword in link.first.name && link.second.containsAll(tags))
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTidsPublic)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun listSearchedLinksWithTids() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinks) {
                if (link.second.containsAll(tags))
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTids)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun listSearchedLinksWithKeyword() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            var assertedResultCount = 0
            for (link in SjTestDataUtil.testLinks) {
                if (keyword in link.first.name)
                    assertedResultCount++
            }

            val result = getValueOrThrow(links, ::postLinksByKeywordAndTids)
            Assert.assertEquals(assertedResultCount, result.size)
        }
    }

    @Test
    fun updateLinkAndTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetLink = SjTestDataUtil.testLinks[2].first
            val updateName = "*쿨한 링크 이름*"
            val updateTags = listOf(
                SjTestDataUtil.testTags[4],
                SjTestDataUtil.testTags[1]
            )
            val updatedLink = targetLink.copy(name = updateName)
            linkRepo.updateLinkAndTags(null, updatedLink, updateTags).join()

            val result = getValueOrThrow(links, ::postAllLinks)
            for (link in result) {
                if (link.link.lid == targetLink.lid) {
                    Assert.assertEquals(updateName, link.link.name)
                    val tempTags = mutableListOf<SjTag>()
                    tempTags.addAll(link.tags)
                    tempTags.removeAll(updateTags)
                    Assert.assertEquals(true,tempTags.isEmpty())
                    break
                }
            }
        }
    }


    @Test
    fun deleteLinkByLid() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetLink = SjTestDataUtil.testLinks[0]
            linkRepo.deleteLinkByLid(targetLink.first.lid).join()

            val result = getValueOrThrow(links, ::postAllLinks)
            Assert.assertEquals(
                SjTestDataUtil.testLinks.size - 1, result.size
            )
        }
    }


}