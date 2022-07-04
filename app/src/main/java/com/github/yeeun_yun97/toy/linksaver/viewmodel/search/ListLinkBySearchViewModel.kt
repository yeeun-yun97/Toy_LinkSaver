package com.github.yeeun_yun97.toy.linksaver.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.link.SjListLinkRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.search.SjSearchSetRepository
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.tag.SjListTagGroupRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// enum to save mode status
enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}

@HiltViewModel
class ListLinkBySearchViewModel @Inject constructor(
    private val searchSetRepo: SjSearchSetRepository,
    private val linkRepo: SjListLinkRepository,
    private val tagRepo: SjListTagGroupRepository
) : SjUsePrivateModeViewModelImpl() {

    // mode
    private lateinit var mode: ListMode

    // binding
    val bindingSearchWord = MutableLiveData<String>()
    private val _bindingSearchTags = MutableLiveData<List<SjTag>>()
    val bindingSearchTags: LiveData<List<SjTag>> get() = _bindingSearchTags

    private lateinit var _searchTagMap: MutableMap<Int, SjTag>
    private val _tids: List<Int> get() = _searchTagMap.keys.toList()


    // data
    val links = linkRepo.links
    val bindingSearchSets = searchSetRepo.searchSetList

    val defaultTags = tagRepo.defaultGroup
    val tagGroups = tagRepo.tagGroupsWithoutDefault

    init {
        initValuesAndSetModeAll()
    }

    private fun initValuesAndSetModeAll() {
        this.mode = ListMode.MODE_ALL
        this.bindingSearchWord.postValue("")
        _searchTagMap = mutableMapOf()
        updateTags()
    }

    override fun refreshData() {
        refreshLinks()
        refreshSearchSets()
        refreshTags()
        refreshDefaultTags()
    }

    private fun refreshDefaultTags() {
        tagRepo.postDefaultTagGroup()
    }

    private fun refreshTags() {
        when (isPrivateMode) {
            true -> tagRepo.postTagGroupsNotDefaultPublic()
            false -> tagRepo.postTagGroupsNotDefault()
        }
    }

    private fun refreshSearchSets() {
        when (isPrivateMode) {
            true -> searchSetRepo.postSearchSetPublic()
            false -> searchSetRepo.postAllSearchSet()
        }
    }

    private fun refreshLinks() {
        when (mode) {
            ListMode.MODE_ALL -> {
                when (isPrivateMode) {
                    true -> linkRepo.postLinksPublic()
                    false -> linkRepo.postAllLinks()
                }
            }
            ListMode.MODE_SEARCH -> {
                when (isPrivateMode) {
                    true -> linkRepo.postLinksByKeywordAndTidsPublic(
                        bindingSearchWord.value!!,
                        _tids
                    )
                    false -> linkRepo.postLinksByKeywordAndTids(bindingSearchWord.value!!, _tids)
                }
            }
        }
    }

    // set search data
    fun setSearch(keyword: String, tags: List<SjTag>) =
        viewModelScope.launch(Dispatchers.Main) {
            val keywordJob = launch { bindingSearchWord.postValue(keyword) }
            val tagsJob = launch {
                _searchTagMap.clear()
                for (tag in tags) _searchTagMap[tag.tid] = tag
            }
            keywordJob.join()
            tagsJob.join()
            updateTags()
        }


    // search methods
    fun isSearchSetEmpty(): Boolean =
        bindingSearchWord.value!!.isEmpty() && _searchTagMap.isEmpty()

    fun startSearchAndSaveIfNotEmpty() =
        CoroutineScope(Dispatchers.IO).launch {
            updateTags()
            if (isSearchSetEmpty()) {
                initValuesAndSetModeAll()
            } else {
                Log.d("모드 변경", "Search")
                mode = ListMode.MODE_SEARCH
                refreshLinks()
                searchSetRepo.insertSearchSet(keyword = bindingSearchWord.value!!, tids = _tids)
                    .join()
            }
        }

    fun clearSearchSet() {
        initValuesAndSetModeAll()
        refreshLinks()
    }


    // delete searchSets
    fun deleteAllSearchSet() =
        viewModelScope.launch {
            val deleteJob = searchSetRepo.deleteAllSearchSet()
            launch(Dispatchers.Main) {
                deleteJob.join()
                refreshSearchSets()
            }
        }


    // manage tag selection
    fun addTag(tag: SjTag) {
        _searchTagMap[tag.tid] = tag
    }

    private fun updateTags() {
        _bindingSearchTags.postValue(_searchTagMap.values.toList())
    }

    fun removeTag(tag: SjTag) {
        _searchTagMap.remove(tag.tid)
    }

    fun containsTag(tag: SjTag) = _searchTagMap.containsKey(tag.tid)


}

