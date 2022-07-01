package com.github.yeeun_yun97.toy.linksaver.test.repository

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearchWithTags
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

@HiltAndroidTest
class SearchRepositoryTest : SjRepositoryBaseTest<SjSearchWithTags>() {
    override fun targetLiveData(): LiveData<List<SjSearchWithTags>> = searchSetRepo.searchSetList

    @Test
    fun listAllSearchSets() {
        assertLiveDataUpdatedSize(
            ::postAllSearchSets,
            SjTestDataUtil.testSearchSets.size
        )
    }
    private fun postAllSearchSets() = searchSetRepo.postAllSearchSet()

    @Test
    fun listSearchSetsPublic() {
        assertLiveDataUpdatedSize(
            ::postSearchSetsPublic,
            SjTestDataUtil.testSearchSetsPublic.size
        )
    }
    private fun postSearchSetsPublic() = searchSetRepo.postSearchSetPublic()

    @Test
    fun deleteAll() {
        assertLiveDataUpdatedSize(
            ::postAllAfterDelete,
            0
        )
    }
    private fun postAllAfterDelete() =
        CoroutineScope(Dispatchers.IO).launch {
            searchSetRepo.deleteAllSearchSet().join()
            searchSetRepo.postAllSearchSet().join()
        }


}