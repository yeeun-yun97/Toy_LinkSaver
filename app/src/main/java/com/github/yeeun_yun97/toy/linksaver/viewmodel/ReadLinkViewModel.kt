package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class ReadLinkViewModel : BasicViewModelWithRepository() {
    val linkList get() = repository.linkList
    val searchLinkList get() = repository.searchLinkList
    var mode: ListMode = ListMode.MODE_ALL

    val tagList = repository.tags
    var selectedTags=mutableListOf<SjTag>()
    val searchList = repository.searches


    fun saveSearch(newSearch: SjSearch){
        repository.saveSearchAndTags(newSearch,selectedTags)
        //selectedTags를 clear해서 하나의 객체로 썼었는데,
        //일이 비동기로 일어나다 보니, 다른 검색을 하는 중에 clear()하는 사고가 발생해서
        //어쩔 수 없이 일단 새 객체로 갈아서 다음 검색은 다른 리스트를 사용하게 만들어 보았다.
        selectedTags= mutableListOf()
    }

    fun deleteAllSearch(){
        repository.deleteSearch()
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) {
        repository.deleteLink(link, tags)
    }

    fun searchLinkBySearchSet(keyword:String){
        this.mode = ListMode.MODE_SEARCH
        repository.searchLinksBySearchSet(keyword,selectedTags)
    }

}

enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}