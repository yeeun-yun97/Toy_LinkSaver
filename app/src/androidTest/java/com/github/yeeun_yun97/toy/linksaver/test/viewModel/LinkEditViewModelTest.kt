package com.github.yeeun_yun97.toy.linksaver.test.viewModel

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.edit.LinkEditViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test

@HiltAndroidTest
class LinkEditViewModelTest : SjBaseTest() {

    private lateinit var viewModel: LinkEditViewModel
    private val targetLink = SjTestDataUtil.testLinks[2]

    override fun before() {
        viewModel = LinkEditViewModel(
            tagListRepo,
            editLinkRepo,
            networkRepo
        )
    }

    @Test
    fun setLidTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.lid = targetLink.first.lid
            val result = getValueOrThrow(viewModel.bindingName)


        }
    }

    @Test
    fun setPrivateTest() {
    }


    @Test
    fun setUrlTest() {
    }

    @Test
    fun createTagTest() {
    }

    @Test
    fun saveLinkTest() {
    }


}