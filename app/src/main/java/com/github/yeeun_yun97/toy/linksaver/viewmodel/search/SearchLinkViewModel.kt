package com.github.yeeun_yun97.toy.linksaver.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

// enum to save mode status
enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}

class SearchLinkViewModel : BasicViewModelWithRepository() {
    val linkList = repository.linkList
    val searchLinkList = repository.searchLinkList
    val searchList = repository.searches
    val tagList = repository.tags

    // mode
    var mode: ListMode = ListMode.MODE_ALL

    // to save
    val bindingSearchWord = MutableLiveData("")
    private var _selectedTags = mutableListOf<SjTag>()
    private val _targetTags = MutableLiveData(_selectedTags)
    val targetTags: LiveData<MutableList<SjTag>> get() = _targetTags

    //  update tag selection
    fun addTag(tag: SjTag) {
        _selectedTags.add(tag)
    }

    fun removeTag(tag: SjTag) {
        _selectedTags.remove(tag)
    }

    fun containsTag(tag: SjTag) = _selectedTags.contains(tag)

    fun setTags(tags: List<SjTag>) {
        _selectedTags.clear()
        _selectedTags.addAll(tags)
        _targetTags.postValue(_selectedTags)
    }

    private fun initTags() {
        _selectedTags = mutableListOf()
        _targetTags.postValue(_selectedTags)
    }


    // search methods
    fun searchLinkBySearchSetAndSave() {
        if (isSearchSetEmpty()) {
            this.mode = ListMode.MODE_ALL
            initData()
        } else {
            this.mode = ListMode.MODE_SEARCH
            searchLinkBySearchSet()
            saveSearchSet()
        }
    }

    fun searchLinkBySearchSet() {
        val keyword = bindingSearchWord.value!!
        repository.searchLinksBySearchSet(keyword, _selectedTags)
    }

    fun isSearchSetEmpty(): Boolean =
        bindingSearchWord.value.isNullOrEmpty() && _selectedTags.isEmpty()

    private fun saveSearchSet() {
        // save
        repository.insertSearchAndTags(
            this.bindingSearchWord.value!!,
            this._selectedTags
        )
    }

    private fun initData() {
        this.bindingSearchWord.postValue("")
        this.initTags()
    }


    // delete methods
    fun deleteAllSearch() = repository.deleteSearch()

    fun deleteLink(link: SjLink, tags: List<SjTag>) = repository.deleteLink(link, tags)
}

