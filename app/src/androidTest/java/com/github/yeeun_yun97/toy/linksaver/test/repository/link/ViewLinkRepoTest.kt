package com.github.yeeun_yun97.toy.linksaver.test.repository.link

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ViewLinkRepoTest : SjBaseTest() {
    private lateinit var link: LiveData<LinkDetailValue>

    override fun before() {
        link = linkRepo.link
    }

    private val targetLink = SjTestDataUtil.testLinks[2].first

    private fun postLink() =
        linkRepo.postLink(targetLink.lid)

    @Test
    fun listLink() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val result = getValueOrThrow(link, ::postLink)
            Assert.assertEquals(targetLink.lid, result.lid)
            Assert.assertEquals(targetLink.name, result.name)
            Assert.assertEquals(targetLink.url, result.url)
        }
    }

    @Test
    fun deleteLinkByLid() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            linkRepo.deleteLinkByLid(targetLink.lid).join()
            delay(3000)

            val result = countRepo.getLinkCount()
            Assert.assertEquals(SjTestDataUtil.testLinks.size-1,result)
        }
    }


}