package com.github.yeeun_yun97.toy.linksaver.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjRoomRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// enum to save mode status
enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}

class SearchLinkViewModel : BasicViewModelWithRepository() {
    private val roomRepository = SjRoomRepository.getInstance()


    // mode
    private var mode: ListMode = ListMode.MODE_ALL
    var isPrivateMode: Boolean? = false


    // binding
    val bindingSearchWord = MutableLiveData("")
    private var _searchTags = mutableListOf<SjTag>()
    private val _bindingSearchTags = MutableLiveData(_searchTags)
    val bindingSearchTags: LiveData<MutableList<SjTag>> get() = _bindingSearchTags


    // data
    private val _dataList = MutableLiveData<List<SjLinksAndDomainsWithTags>>()
    val dataList: LiveData<List<SjLinksAndDomainsWithTags>> get() = _dataList


    fun refreshData() {
        //if (isSearchSetEmpty()) mode = ListMode.MODE_ALL
        when (mode) {
            ListMode.MODE_ALL -> loadAllLinks()
            ListMode.MODE_SEARCH -> loadSearchLinks()
        }
    }

    private fun loadAllLinks() {
        viewModelScope.launch(Dispatchers.IO) {
            val links =
                if (isPrivateMode == true) {
                    Log.d("똥", "숨김/전체모드")
                    roomRepository.selectLinksPublic()
                } else {
                    Log.d("똥", "전체모드")
                    roomRepository.selectAllLinks()
                }
            Log.d("똥", "값= ${links.toString()}")
            _dataList.postValue(links)
        }
    }

    private fun loadSearchLinks() {
        viewModelScope.launch(Dispatchers.IO) {
            val links =
                if (isPrivateMode == true) {
                    Log.d("똥", "숨김/검색모드 ${bindingSearchWord.value!!}")
                    roomRepository.selectLinksByNameAndTagsPublic(
                        bindingSearchWord.value!!,
                        _searchTags
                    )
                } else {
                    Log.d("똥", "검색모드 ${bindingSearchWord.value!!}")
                    roomRepository.selectLinksByNameAndTags(
                        bindingSearchWord.value!!,
                        _searchTags
                    )
                }
            _dataList.postValue(links)
        }
    }

    fun clearSearchSet() {
        initData()
        refreshData()
    }

    private fun initData() {
        this.mode = ListMode.MODE_ALL
        this.bindingSearchWord.postValue("")
        _searchTags = mutableListOf()
        _bindingSearchTags.postValue(_searchTags)
    }

    // search methods
    fun startSearchAndSaveIfNotEmpty() {
        if (isSearchSetEmpty()) {
            initData()
        } else {
            this.mode = ListMode.MODE_SEARCH
            searchAndSave()
        }
    }

    private fun searchAndSave() {
        refreshData()
        repository.insertSearchAndTags(this.bindingSearchWord.value!!, this._searchTags)
    }

    //  update tag selection
    fun addTag(tag: SjTag) {
        _searchTags.add(tag)
        _bindingSearchTags.postValue(_searchTags)
    }

    fun removeTag(tag: SjTag) {
        _searchTags.remove(tag)
        _bindingSearchTags.postValue(_searchTags)
    }

    fun containsTag(tag: SjTag) = _searchTags.contains(tag)

    fun setTags(tags: List<SjTag>) {
        _searchTags.clear()
        _searchTags.addAll(tags)
        _bindingSearchTags.postValue(_searchTags)
    }

    fun isSearchSetEmpty(): Boolean =
        bindingSearchWord.value.isNullOrEmpty() && _searchTags.isEmpty()

    // delete methods
    fun deleteAllSearch() = repository.deleteSearch()

    //////////

    // search lists
    val searchList = repository.searches
    val publicSearchList = repository.publicSearches

    // default group
    val tagDefaultGroup = repository.defaultTagGroup

    // tag groups
    val tagGroups = repository.tagGroups
    val publicTagGroups = repository.publicTagGroups


//    private fun refreshSearch() {
//        val keyword = bindingSearchWord.value!!
//        repository.searchLinksBySearchSet(keyword, _searchTags, isPrivateMode!!)
//    }


}

