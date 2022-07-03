package com.github.yeeun_yun97.toy.linksaver.test.repository.search

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
class SearchSetRepoTest : SjBaseTest() {
    private lateinit var searchSets: LiveData<List<SjSearchWithTags>>

    override fun before() { searchSets = searchSetListRepo.searchSetList }

    private fun postAllSearchSets() = searchSetListRepo.postAllSearchSet()
    private fun postSearchSetsPublic() = searchSetListRepo.postSearchSetPublic()

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
            searchSetListRepo.deleteAllSearchSet().join()
            val result = getValueOrThrow(searchSets, ::postAllSearchSets)
            Assert.assertEquals(
                0,
                result.size
            )
        }
    }

}