package com.github.yeeun_yun97.toy.linksaver.test.viewModel.link

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.link.EditLinkViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class EditLinkVMTest : SjBaseTest() {

    private lateinit var viewModel: EditLinkViewModel
    private val targetLink = SjTestDataUtil.testLinks[2]

    override fun before() {
        viewModel = EditLinkViewModel(
            tagListRepo,
            editLinkRepo,
            networkRepo
        )
    }

    @Test
    fun setPrivateOnTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(true)
            delay(3000)

            val result = getValueOrThrow(viewModel.tagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefaultPublic.size, result.size)
        }
    }

    @Test
    fun setPrivateOffTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(false)
            delay(3000)

            val result = getValueOrThrow(viewModel.tagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefault.size, result.size)
        }
    }

    @Test
    fun setLidTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.lid = targetLink.first.lid
            delay(3000)

            val name = getValueOrThrow(viewModel.bindingName)
            Assert.assertEquals(targetLink.first.name, name)
            val url = getValueOrThrow(viewModel.bindingUrl, timeout = 0)
            Assert.assertEquals(targetLink.first.url, url)
        }
    }

    @Test
    fun setUrlTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetUrl = "https://www.google.co.kr/"
            viewModel.url = targetUrl
            delay(3000)

            val url = getValueOrThrow(viewModel.bindingUrl)
            Assert.assertEquals(targetUrl, url)
        }
    }

    @Test
    fun createTagTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val tagName = "*매우 쿨한 태그 이름*"
            viewModel.createTag(tagName).join()
            delay(3000)

            val result = countRepo.getTagCount()
            Assert.assertEquals(SjTestDataUtil.testTags.size + 1, result)
        }
    }

    @Test
    fun saveLinkTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetUrl = "https://www.google.co.kr/"
            val targetName = "*매우 쿨한 링크 이름*"
            viewModel.url = targetUrl
            viewModel.bindingName.postValue(targetName)
            delay(3000)
            viewModel.saveLink()
            delay(3000)

            val result = countRepo.getLinkCount()
            Assert.assertEquals(SjTestDataUtil.testLinks.size + 1, result)
        }
    }

}