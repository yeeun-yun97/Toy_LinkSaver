package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

// enum to save mode status
enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}

class ReadLinkViewModel : BasicViewModelWithRepository() {
    val linkList = repository.linkList
    val searchLinkList = repository.searchLinkList
    val searchList = repository.searches
    val tagList = repository.tags

    // mode
    var mode: ListMode = ListMode.MODE_ALL

    // data binding liveData
    val searchWord = MutableLiveData<String>()

    // selected tags for search
    var selectedTags = mutableListOf<SjTag>()


    // delete methods
    fun deleteAllSearch() = repository.deleteSearch()

    fun deleteLink(link: SjLink, tags: List<SjTag>) = repository.deleteLink(link, tags)


    // search methods
    fun searchLinkBySearchSet() {
        var keyword = searchWord.value ?: ""

        //when search data is null -> return to All list mode
        if (keyword.isEmpty() && selectedTags.isEmpty()) {
            this.mode = ListMode.MODE_ALL
            return
        }

        this.mode = ListMode.MODE_SEARCH
        // search without Tags
        if (selectedTags.isEmpty()) {
            repository.searchLinksBySearchSet(keyword!!)

        // search with Tags
        } else {
            repository.searchLinksBySearchSet(keyword!!, selectedTags)
        }
        saveSearch(keyword!!)
    }

    private fun saveSearch(keyword: String) {
        repository.insertSearchAndTags(
            SjSearch(keyword = keyword),
            selectedTags
        )
        /*
        매번 selectedTags.clear()해서 하나의 객체로 썼었는데,
        일이 비동기로 일어나다 보니, 다른 검색을 하는 중에 clear()하는 사고가 발생해서
        어쩔 수 없이 일단 새 객체로 갈아서 다음 검색은 다른 리스트 객체를 사용하게 만들었다.
         */
        selectedTags = mutableListOf()
    }

}

