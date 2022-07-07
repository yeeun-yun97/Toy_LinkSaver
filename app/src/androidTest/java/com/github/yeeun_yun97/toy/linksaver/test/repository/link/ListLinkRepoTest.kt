package com.github.yeeun_yun97.toy.linksaver.test.repository.link

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ListLinkRepoTest : SjBaseTest() {
    private lateinit var links: LiveData<List<SjLinksAndDomainsWithTags>>

    override suspend fun before() {
        links = linkListRepo.links
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
        linkListRepo.postAllLinks()

    private fun postLinksPublic() =
        linkListRepo.postLinksPublic()

    private fun postLinksByKeywordAndTidsPublic() =
        linkListRepo.postLinksByKeywordAndTidsPublic(keyword, tids)

    private fun postLinksByKeywordAndTids() =
        linkListRepo.postLinksByKeywordAndTids(keyword, tids)

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


}