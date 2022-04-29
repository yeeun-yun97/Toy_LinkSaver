package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjSearch
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class SearchViewModel : BasicViewModelWithRepository() {
    val tagList = repository.tags
    val selectedTags=mutableListOf<SjTag>()
    val searchList = repository.searches


    fun saveSearch(newSearch: SjSearch){
        repository.saveSearchAndTags(newSearch,selectedTags)
    }

    fun deleteAllSearch(){
        repository.deleteSearch()
    }

}