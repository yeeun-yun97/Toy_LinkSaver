package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class SearchViewModel : BasicViewModelWithRepository() {
    val tagList = repository.tags

}