package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class ReadLinkViewModel : BasicViewModelWithRepository() {
    val linkList get() = repository.linkList
    val searchLinkList get() = repository.searchLinkList
    var mode: ListMode = ListMode.MODE_ALL

    fun deleteLink(link: SjLink, tags: List<SjTag>) {
        repository.deleteLink(link, tags)
    }

    fun searchLinkByLinkName(linkName: String) {
        this.mode = ListMode.MODE_SEARCH
        repository.searchLinksByLinkName(linkName)
    }
}

enum class ListMode {
    MODE_ALL, MODE_SEARCH;
}