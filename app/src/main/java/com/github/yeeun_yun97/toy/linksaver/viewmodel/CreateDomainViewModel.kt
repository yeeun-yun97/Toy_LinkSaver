package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class CreateDomainViewModel : BasicViewModelWithRepository() {

    fun insertDomain(newDomain: SjDomain) {
        repository.insertDomain(newDomain)
    }

}