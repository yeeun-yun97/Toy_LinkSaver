package com.github.yeeun_yun97.toy.linksaver.test.repository.link

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.ELinkType
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class EditLinkRepoTest : SjBaseTest() {

    private val targetLink = SjTestDataUtil.testLinks[2].first
    private val targetUrl = "https://www.google.com/"

    override suspend fun before() {}

    private fun postLinkByLid() = editLinkRepo.postLoadedLink(targetLink.lid)
    private fun createLinkByUrl() = editLinkRepo.postCreatedLink(targetUrl)

    @Test
    fun loadLinkTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val resultName =
                getValueOrThrow(editLinkRepo.linkName, ::postLinkByLid)
            Assert.assertEquals(targetLink.name, resultName)
            val resultUrl =
                getValueOrThrow(editLinkRepo.linkUrl, timeout = 0)
            Assert.assertEquals(targetLink.url, resultUrl)
            val resultIsVideo =
                getValueOrThrow(editLinkRepo.linkIsVideo, timeout = 0)
            Assert.assertEquals(targetLink.type == ELinkType.video, resultIsVideo)
        }
    }

    @Test
    fun createLinkTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val resultName = getValueOrThrow(editLinkRepo.linkName, ::createLinkByUrl)
            Assert.assertEquals("", resultName)
            val resultUrl =
                getValueOrThrow(editLinkRepo.linkUrl, timeout = 0)
            Assert.assertEquals(targetUrl, resultUrl)
            val resultIsVideo =
                getValueOrThrow(editLinkRepo.linkIsVideo, timeout = 0)
            Assert.assertEquals(false, resultIsVideo)
        }
    }

    @Test
    fun updateLinkAndTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val updateName = "*쿨한 링크 이름*"
            postLinkByLid().join()
            editLinkRepo.updateName(updateName)
            editLinkRepo.saveLink().join()

            val result = getValueOrThrow(editLinkRepo.linkName, ::postLinkByLid)
            Assert.assertEquals(updateName, result)
        }
    }

    @Test
    fun createLinkAndTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            createLinkByUrl().join()
            editLinkRepo.saveLink().join()
            Assert.assertEquals(SjTestDataUtil.testLinks.size + 1, countRepo.getLinkCount())
            //FIXME WEAK ASSERTION
        }
    }


}