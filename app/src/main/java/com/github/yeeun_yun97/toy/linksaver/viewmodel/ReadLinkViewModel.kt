package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class ReadLinkViewModel : BasicViewModelWithRepository(){
    val linkList get() = repository.linkList

    fun deleteLink(link: SjLink, tags: List<SjTag>) {
        repository.deleteLink(link,tags)
    }

}