package com.github.yeeun_yun97.toy.linksaver.test.repository.tag

import android.util.Log
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
class ListTagGroupRepoTest : SjBaseTest() {
    private lateinit var tagGroupsNotDefault: LiveData<List<SjTagGroupWithTags>>
    private lateinit var defaultTagGroup: LiveData<SjTagGroupWithTags>

    override fun before() {
        tagGroupsNotDefault = tagListRepo.tagGroupsWithoutDefault
        defaultTagGroup = tagListRepo.defaultGroup
    }

    // update repository liveData function
    private fun postAllTags() = tagListRepo.postTagGroupsNotDefault()
    private fun postTagsPublic() = tagListRepo.postTagGroupsNotDefaultPublic()
    private fun postDefaultTags() = tagListRepo.postDefaultTagGroup()

    @Test
    fun listAllTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val resultNotDefault = getValueOrThrow(tagGroupsNotDefault, ::postAllTags)
            Assert.assertEquals(
                SjTestDataUtil.testTagGroupsNotDefault.size,
                resultNotDefault.size
            )
        }
    }

    @Test
    fun listTagsPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val resultNotDefaultPublic = getValueOrThrow(tagGroupsNotDefault, ::postTagsPublic)
            Assert.assertEquals(
                SjTestDataUtil.testTagGroupsNotDefaultPublic.size,
                resultNotDefaultPublic.size
            )
        }
    }

    @Test
    fun listAllDefaultTags() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val result = getValueOrThrow(defaultTagGroup, ::postDefaultTags)
            Assert.assertEquals(SjTestDataUtil.testTagsDefault.size, result.tags.size)
        }
    }

    @Test
    fun updateTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val targetGroup = SjTestDataUtil.testTagGroupsNotDefault[1]
            val newName = "*매우 쿨한 새 그룹 이름*"
            val isPrivate = !targetGroup.isPrivate

            tagListRepo.updateTagGroup(targetGroup.copy(name = newName, isPrivate = isPrivate))
            val resultNotDefault = getValueOrThrow(tagGroupsNotDefault, ::postAllTags)
            for (result in resultNotDefault) {
                if (result.tagGroup.gid == targetGroup.gid) {
                    Assert.assertEquals(newName, result.tagGroup.name)
                    Assert.assertEquals(isPrivate, result.tagGroup.isPrivate)
                    break
                }
            }
        }
    }

    @Test
    fun deleteTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val deleteTarget = SjTestDataUtil.testTagGroupsNotDefault[0]
            Log.d("deleteTarget", "$deleteTarget")
            tagListRepo.deleteTagGroupByGid(deleteTarget.gid).join()

            // check target group deleted
            val resultNotDefault = getValueOrThrow(tagGroupsNotDefault, ::postAllTags)
            for (tagGroup in resultNotDefault) {
                Assert.assertEquals(false, tagGroup.tagGroup.gid == deleteTarget.gid)
            }

            // check target group tags moved to default group
            var expectedCount = SjTestDataUtil.testTagsDefault.size
            for (tag in SjTestDataUtil.testTagsNotDefault) {
                if (tag.gid == deleteTarget.gid) {
                    expectedCount++
                }
            }
            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTags)
            Assert.assertEquals(expectedCount, resultDefault.tags.size)
        }
    }


}