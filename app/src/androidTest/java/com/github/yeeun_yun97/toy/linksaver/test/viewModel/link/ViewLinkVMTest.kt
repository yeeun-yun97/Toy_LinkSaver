package com.github.yeeun_yun97.toy.linksaver.test.viewModel.link

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.link.ViewLinkViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ViewLinkVMTest : SjBaseTest() {
    private lateinit var viewModel: ViewLinkViewModel

    private val targetLink = SjTestDataUtil.testLinks[3].first
    private val targetLinkTags = SjTestDataUtil.testLinks[3].second

    override suspend fun before() {
        viewModel = ViewLinkViewModel(linkRepo)
    }

    @Test
    fun setLid() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            viewModel.lid = targetLink.lid

            val resultName = getValueOrThrow(viewModel.bindingLinkName)
            Assert.assertEquals(targetLink.name, resultName)

            val resultTags = getValueOrThrow(viewModel.bindingTags, timeout = 0)
            val tempList = mutableListOf<SjTag>().apply {
                addAll(targetLinkTags)
                removeAll(resultTags)
            }
            Assert.assertEquals(true, tempList.isEmpty())

            val resultUrl = getValueOrThrow(viewModel.bindingFullUrl, timeout = 0)
            Assert.assertEquals(targetLink.url, resultUrl)
        }
    }

    @Test
    fun deleteLink() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            viewModel.lid = targetLink.lid
            viewModel.deleteLink()

            delay(3000)
            val result = countRepo.getLinkCount()
            Assert.assertEquals(SjTestDataUtil.testLinks.size - 1, result)
        }
    }

}