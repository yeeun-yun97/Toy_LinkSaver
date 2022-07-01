package com.github.yeeun_yun97.toy.linksaver.test.repository

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
class TagRepositoryTest : SjBaseTest() {
    private lateinit var tagGroupsNotDefault: LiveData<List<SjTagGroupWithTags>>
    private lateinit var defaultTagGroup: LiveData<SjTagGroupWithTags>

    override fun before() {
        super.before()
        tagGroupsNotDefault = tagRepo.tagGroupsWithoutDefault
        defaultTagGroup = tagRepo. defaultGroup

    }

    // update repository liveData function
    private fun postAllTags() = tagRepo.postTagGroupsNotDefault()
    private fun postTagsPublic() = tagRepo.postTagGroupsPublicNotDefault()
    private fun postDefaultTags() = tagRepo.postDefaultTagGroup()


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
    fun updateTag() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val targetTag = SjTestDataUtil.testTagsNotDefault[3]
            val newName = "*매우 쿨한 새 태그 이름*"
            val defaultGid = 1

            tagRepo.updateTag(targetTag.copy(name = newName, gid = defaultGid))
            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTags)
            for (result in resultDefault.tags) {
                if (result.tid == targetTag.tid) {
                    Assert.assertEquals(newName, result.name)
                    break
                }
            }
        }
    }

    @Test
    fun updateTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val targetGroup = SjTestDataUtil.testTagGroupsNotDefault[1]
            val newName = "*매우 쿨한 새 그룹 이름*"
            val isPrivate = !targetGroup.isPrivate

            tagRepo.updateTagGroup(targetGroup.copy(name = newName, isPrivate = isPrivate))
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
    fun moveTagsToTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val targetGid = 1 // default group id
            val targetTags =
                listOf(SjTestDataUtil.testTagsNotDefault[1], SjTestDataUtil.testTagsNotDefault[4])
            tagRepo.updateTagsToGid(tags = targetTags, gid = targetGid).join()
            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTags)
            Assert.assertEquals(
                SjTestDataUtil.testTagsDefault.size + targetTags.size,
                resultDefault.tags.size
            )
        }
    }

    @Test
    fun deleteTagGroup() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val deleteTarget = SjTestDataUtil.testTagGroupsNotDefault[0]
            Log.d("deleteTarget","${deleteTarget}")
            tagRepo.deleteTagGroupByGid(deleteTarget.gid).join()

            // check target group deleted
            val resultNotDefault = getValueOrThrow(tagGroupsNotDefault, ::postAllTags)
            for (tagGroup in resultNotDefault) {
                Assert.assertEquals(false, tagGroup.tagGroup.gid == deleteTarget.gid)
            }

            // check target group tags moved to default group
            var expectedCount = SjTestDataUtil.testTagsDefault.size
            Log.d("expectedCount updated - ","by default, $expectedCount")
            Log.d("expectedCount tags - ","${SjTestDataUtil.testTagsDefault}")

            Log.d("check rest tags","${SjTestDataUtil.testTagsNotDefault}")
            for (tag in SjTestDataUtil.testTagsNotDefault) {
                if (tag.gid == deleteTarget.gid) {
                    expectedCount++
                    Log.d("expectedCount updated - ","by tag ${tag}, $expectedCount")
                }
            }
            val resultDefault = getValueOrThrow(defaultTagGroup,::postDefaultTags)
            Log.d("result - ","${resultDefault.tags}")
            Assert.assertEquals(expectedCount, resultDefault.tags.size)
        }
    }

    @Test
    fun deleteTag() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val deleteTarget = SjTestDataUtil.testTags[0]
            tagRepo.deleteTagByTid(deleteTarget.tid).join()
            val resultNotDefault = getValueOrThrow(tagGroupsNotDefault, ::postAllTags)
            for (group in resultNotDefault)
                Assert.assertEquals(false, group.tags.contains(deleteTarget))
            val resultDefault = getValueOrThrow(defaultTagGroup, ::postDefaultTags)
            Assert.assertEquals(false, resultDefault.tags.contains(deleteTarget))
            // TODO 태그 레퍼런스 체크도 해야할까?
            //   그러려면 link나 search 라이브데이터 가져와야 하는데 배보다 배꼽이 큰 건 아닌지..
        }
    }

}