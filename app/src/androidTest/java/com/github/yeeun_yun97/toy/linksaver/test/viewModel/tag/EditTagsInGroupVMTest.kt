package com.github.yeeun_yun97.toy.linksaver.test.viewModel.tag

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.tag.EditTagsInGroupViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class EditTagsInGroupVMTest : SjBaseTest() {
    private lateinit var viewModel: EditTagsInGroupViewModel

    private val targetGroup = SjTestDataUtil.testTagGroupsNotDefault[1]

    override suspend fun before() {
        viewModel = EditTagsInGroupViewModel(tagGroupRepo)
    }

    @Test
    fun setGidTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid

            val result = getValueOrThrow(viewModel.tagGroup)
            Assert.assertEquals(result.tagGroup.gid, targetGroup.gid)
            Assert.assertEquals(result.tagGroup.name, targetGroup.name)
        }
    }

    @Test
    fun deleteTagTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid
            val data = getValueOrThrow(viewModel.tagGroup)
            val targetTag = data.tags[0]
            viewModel.deleteTag(targetTag)

            val result = getValueOrThrow(viewModel.tagGroup)
            Assert.assertEquals(false, result.tags.contains(targetTag))
        }
    }

    @Test
    fun createTagTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid
            val name = "*쿨한 태그 이름*"
            viewModel.editTagToGroup(null, name)

            val result = getValueOrThrow(viewModel.tagGroup)
            var found = false
            for (tag in result.tags) {
                if (tag.name == name) {
                    found = true
                    break
                }
            }
            Assert.assertEquals(true, found)
        }
    }

    @Test
    fun updateTagTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.gid = targetGroup.gid
            val name = "*쿨한 태그 이름*"
            val data = getValueOrThrow(viewModel.tagGroup)
            val targetTag = data.tags[0]
            viewModel.editTagToGroup(targetTag, name)

            val result = getValueOrThrow(viewModel.tagGroup)
            var found = false
            for (tag in result.tags) {
                if (tag.tid == targetTag.tid) {
                    found = tag.name == name
                    break
                }
            }
            Assert.assertEquals(true, found)
        }
    }


}