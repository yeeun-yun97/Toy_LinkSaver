package com.github.yeeun_yun97.toy.linksaver.test.viewModel.tag

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.ListGroupViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ListGroupVMTest : SjBaseTest() {
    private lateinit var viewModel: ListGroupViewModel

    override fun before() {
        viewModel = ListGroupViewModel(tagListRepo)
    }

    @Test
    fun setPrivateModeOnTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(true)
            val result = getValueOrThrow(viewModel.bindingTagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefaultPublic.size, result.size)
        }
    }

    @Test
    fun setPrivateModeOffTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(false)
            val result = getValueOrThrow(viewModel.bindingTagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefault.size, result.size)
        }
    }

    @Test
    fun newTagGroupTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.editTagGroup("*매우 쿨한 그룹 이름*", false, null).join()

            val result = getValueOrThrow(viewModel.bindingTagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefault.size + 1, result.size)
        }
    }

    @Test
    fun updateTagGroupTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetGroup = SjTestDataUtil.testTagGroups[2]
            val newName = "*매우 쿨한 그룹 이름*"
            viewModel.editTagGroup(newName, false, targetGroup).join()

            val result = getValueOrThrow(viewModel.bindingTagGroups)
            for (group in result) {
                if (group.tagGroup.gid == targetGroup.gid) {
                    Assert.assertEquals(newName, group.tagGroup.name)
                    break
                }
            }
        }
    }

    @Test
    fun deleteTagGroupTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetGroup = SjTestDataUtil.testTagGroups[2]
            viewModel.deleteTagGroup(targetGroup.gid).join()

            val result = getValueOrThrow(viewModel.bindingTagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefault.size - 1, result.size)
        }
    }

}