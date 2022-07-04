package com.github.yeeun_yun97.toy.linksaver.test.viewModel.search

import com.github.yeeun_yun97.toy.linksaver.data.SjTestDataUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.test.SjBaseTest
import com.github.yeeun_yun97.toy.linksaver.viewmodel.search.ListLinkBySearchViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@HiltAndroidTest
class ListLinkBySearchVMTest : SjBaseTest() {
    private lateinit var viewModel: ListLinkBySearchViewModel

    override fun before() {
        viewModel = ListLinkBySearchViewModel(searchSetListRepo, linkListRepo, tagListRepo)
    }

    @Test
    fun setPrivateOnTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(true)

            val tagGroups = getValueOrThrow(viewModel.tagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefaultPublic.size, tagGroups.size)
            val links = getValueOrThrow(viewModel.links)
            Assert.assertEquals(SjTestDataUtil.testLinksPublic.size, links.size)
        }
    }

    @Test
    fun setPrivateOffTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(false)

            val tagGroups = getValueOrThrow(viewModel.tagGroups)
            Assert.assertEquals(SjTestDataUtil.testTagGroupsNotDefault.size, tagGroups.size)
            val links = getValueOrThrow(viewModel.links)
            Assert.assertEquals(SjTestDataUtil.testLinks.size, links.size)
        }
    }

    @Test
    fun setSearchTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val keyword = "검색"
            val tags = listOf<SjTag>()
            viewModel.setSearch(keyword, tags).join()

            val searchWord = getValueOrThrow(viewModel.bindingSearchWord)
            Assert.assertEquals(keyword, searchWord)
            val searchTags = getValueOrThrow(viewModel.bindingSearchTags)
            Assert.assertEquals(tags.size, searchTags.size)
        }
    }

    @Test
    fun searchSaveTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val keyword = "검색"
            val tags = listOf(
                SjTestDataUtil.testTags[0],
                SjTestDataUtil.testTags[2],
            )
            viewModel.setSearch(keyword, tags).join()
            viewModel.startSearchAndSaveIfNotEmpty().join()

            val searchCount = countRepo.getSearchSetCount()
            Assert.assertEquals(SjTestDataUtil.testSearchSets.size + 1 , searchCount)
        }
    }

    @Test
    fun searchNotSaveTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            val targetSearch = SjTestDataUtil.testSearchSetNoTags[0]
            val keyword = targetSearch.keyword
            viewModel.setSearch(keyword, listOf()).join()
            viewModel.startSearchAndSaveIfNotEmpty().join()

            val searchCount = countRepo.getSearchSetCount()
            Assert.assertEquals(SjTestDataUtil.testSearchSets.size , searchCount)
        }
    }

    @Test
    fun cancelSearchByEmptyTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.setPrivate(false)
            val prevCount = getValueOrThrow(viewModel.links).size
            viewModel.startSearchAndSaveIfNotEmpty().join()

            val result = getValueOrThrow(viewModel.links).size
            Assert.assertEquals(prevCount, result)
        }
    }

    @Test
    fun clearSelectedSearchSetTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.clearSearchSet()
            delay(3000)

            Assert.assertEquals(true,viewModel.isSearchSetEmpty())
        }
    }

    @Test
    fun deleteAllSearchSetTest() {
        runBlocking(Dispatchers.Main) {
            insertBaseData().join()

            viewModel.deleteAllSearchSet().join()
            delay(3000)

            val result = countRepo.getSearchSetCount()
            Assert.assertEquals(0, result)
        }
    }


}