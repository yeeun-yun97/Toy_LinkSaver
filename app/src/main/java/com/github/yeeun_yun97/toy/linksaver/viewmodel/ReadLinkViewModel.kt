package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class ReadLinkViewModel : BasicViewModelWithRepository(){
    val linksWithDomains get() = repository.linksWithDomains

    fun deleteLink(link: SjLink) {
        repository.deleteLink(link)
    }

}