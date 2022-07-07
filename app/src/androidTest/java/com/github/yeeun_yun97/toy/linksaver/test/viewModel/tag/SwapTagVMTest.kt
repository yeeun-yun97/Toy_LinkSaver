package com.github.yeeun_yun97.toy.linksaver.test.viewModel.tag

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.SwapTagViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class SwapTagVMTest : SjBaseTest() {
    private lateinit var viewModel: SwapTagViewModel

    private val targetGroup = SjTestDataUtil.testTagGroupsNotDefault[0]


    override suspend fun before() {
        viewModel = SwapTagViewModel(tagGroupRepo)
    }

    @Test
    fun setGidTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid

            val result = getValueOrThrow(viewModel.bindingTargetTagGroup)
            Assert.assertEquals(result.tagGroup.gid, targetGroup.gid)
            Assert.assertEquals(result.tagGroup.name, targetGroup.name)
        }
    }

    @Test
    fun moveTargetTagsTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid
            val targetTags = listOf(
                SjTestDataUtil.testTagsNotDefault[0]
            )
            viewModel.selectedTargetTags.addAll(targetTags)
            viewModel.moveSelectedTargetTagsToBasicGroup()

            val result = getValueOrThrow(viewModel.bindingBasicTagGroup)
            Assert.assertEquals(
                SjTestDataUtil.testTagsDefault.size + targetTags.size,
                result.tags.size
            )
        }
    }

    @Test
    fun moveDefaultTagsTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid
            val targetTags = listOf(
                SjTestDataUtil.testTagsDefault[0]
            )
            viewModel.selectedBasicTags.addAll(targetTags)
            viewModel.moveSelectedBasicTagsToTargetGroup()

            val result = getValueOrThrow(viewModel.bindingBasicTagGroup)
            Assert.assertEquals(
                SjTestDataUtil.testTagsDefault.size - targetTags.size,
                result.tags.size
            )
        }

    }


}