package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.ViewModel
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjRepository

class CreateDomainViewModel :ViewModel(){
    private val repository = SjRepository()

    fun insertDomain(newDomain: SjDomain) {
        repository.insertDomain(newDomain)
    }
}