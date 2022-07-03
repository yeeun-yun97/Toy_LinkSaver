package com.github.yeeun_yun97.toy.linksaver.test.repository.link

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class EditLinkRepoTest : SjBaseTest() {

    private val targetLink = SjTestDataUtil.testLinks[2].first
    private val targetUrl = "https://www.google.com/"

    override fun before() {}

    private fun postLinkByLid() = editLinkRepo.postLoadedLink(targetLink.lid)
    private fun createLinkByUrl() = editLinkRepo.postCreatedLink(targetUrl)

    @Test
    fun createLinkTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val result = getValueOrThrow(editLinkRepo.editLink, ::postLinkByLid)
            Assert.assertEquals(targetLink, result)
        }
    }

    @Test
    fun loadLinkTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val result = getValueOrThrow(editLinkRepo.editLink, ::createLinkByUrl)
            Assert.assertEquals(targetUrl, result.url)
            Assert.assertEquals(0, result.lid)
            Assert.assertEquals(1, result.did)
        }
    }

    @Test
    fun updateLinkAndTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val updateName = "*쿨한 링크 이름*"
            val updateTags = listOf(
                SjTestDataUtil.testTags[4],
                SjTestDataUtil.testTags[1]
            )

            postLinkByLid().join()
            editLinkRepo.updateName(updateName)
            editLinkRepo.editLinkAndTags(null, updateTags).join()

            val result = getValueOrThrow(editLinkRepo.loadedLinkData, ::postLinkByLid)
            Assert.assertEquals(updateName, result.link.name)
            val tempTags = mutableListOf<SjTag>().apply {
                addAll(result.tags)
                removeAll(updateTags)
            }
            Assert.assertEquals(true, tempTags.isEmpty())
        }
    }

    @Test
    fun createLinkAndTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            createLinkByUrl().join()
            editLinkRepo.editLinkAndTags(null, listOf()).join()
            Assert.assertEquals(SjTestDataUtil.testLinks.size + 1, countRepo.getLinkCount())
            //FIXME WEAK ASSERTION
        }
    }


}