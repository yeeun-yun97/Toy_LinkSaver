package com.github.yeeun_yun97.toy.linksaver.test.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class SearchRepositoryTest : SjBaseTest() {
    private lateinit var searchSets: LiveData<List<SjSearchWithTags>>

    override fun before() { searchSets = searchSetRepo.searchSetList }

    private fun postAllSearchSets() = searchSetRepo.postAllSearchSet()
    private fun postSearchSetsPublic() = searchSetRepo.postSearchSetPublic()

    @Test
    fun listAllSearchSets() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val result = getValueOrThrow(searchSets, ::postAllSearchSets)
            Assert.assertEquals(
                SjTestDataUtil.testSearchSets.size,
                result.size
            )
        }
    }


    @Test
    fun listSearchSetsPublic() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            val result = getValueOrThrow(searchSets, ::postSearchSetsPublic)
            Assert.assertEquals(
                SjTestDataUtil.testSearchSetsPublic.size,
                result.size
            )
        }
    }


    @Test
    fun deleteAll() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()
            searchSetRepo.deleteAllSearchSet().join()
            val result = getValueOrThrow(searchSets, ::postAllSearchSets)
            Assert.assertEquals(
                0,
                result.size
            )
        }
    }

}