package com.github.yeeun_yun97.toy.linksaver.test.repository.tag

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTagGroupWithTags
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ViewTagGroupRepoTest : SjBaseTest() {
    private lateinit var targetTagGroup: LiveData<SjTagGroupWithTags>
    private lateinit var defaultTagGroup: LiveData<SjTagGroupWithTags>

    override suspend fun before() {
        targetTagGroup = tagGroupRepo.targetTagGroup
        defaultTagGroup = tagGroupRepo.defaultGroup
    }

    private val tagGroup = SjTestDataUtil.testTagGroupsNotDefault[0]
    private fun postTargetTagGroup() = tagGroupRepo.postTargetTagGroup(tagGroup.gid)
    private fun postDefaultTagGroup() = tagGroupRepo.postDefaultTagGroup()

    @Test
    fun loadTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val result = getValueOrThrow(targetTagGroup, ::postTargetTagGroup, timeout = 5000)
            Assert.assertEquals(tagGroup.gid, result.tagGroup.gid)
            Assert.assertEquals(tagGroup.name, result.tagGroup.name)
            Assert.assertEquals(tagGroup.isPrivate, result.tagGroup.isPrivate)
        }
    }

    @Test
    fun loadDefaultTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val result = getValueOrThrow(defaultTagGroup, ::postDefaultTagGroup)
            Assert.assertEquals(SjTestDataUtil.testTagsDefault.size, result.tags.size)
        }
    }

    @Test
    fun moveTagsToTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetGid = 1 // default group id
            val targetTags =
                listOf(SjTestDataUtil.testTagsNotDefault[1], SjTestDataUtil.testTagsNotDefault[4])
            tagGroupRepo.updateTagsToGid(tags = targetTags, gid = targetGid).join()

            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTagGroup)
            Assert.assertEquals(
                SjTestDataUtil.testTagsDefault.size + targetTags.size,
                resultDefault.tags.size
            )
        }
    }


    @Test
    fun updateTag() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetTag = SjTestDataUtil.testTagsNotDefault[3]
            val newName = "*매우 쿨한 새 태그 이름*"
            val defaultGid = 1
            tagGroupRepo.updateTag(targetTag.copy(name = newName, gid = defaultGid))

            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTagGroup)
            for (result in resultDefault.tags) {
                if (result.tid == targetTag.tid) {
                    Assert.assertEquals(newName, result.name)
                    break
                }
            }
        }
    }

    @Test
    fun deleteTag() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val deleteTarget = SjTestDataUtil.testTagsDefault[0]
            tagGroupRepo.deleteTagByTid(deleteTarget.tid).join()

            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTagGroup)
            Assert.assertEquals(false, resultDefault.tags.contains(deleteTarget))
        }
    }
}