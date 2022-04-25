package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.ViewModel
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjRepository

class ReadLinkViewModel : ViewModel() {
    val repository: SjRepository = SjRepository()
    val linksWithDomains get() = repository.linksWithDomains

    fun deleteLink(link: SjLink) {
        repository.deleteLink(link)
    }


}