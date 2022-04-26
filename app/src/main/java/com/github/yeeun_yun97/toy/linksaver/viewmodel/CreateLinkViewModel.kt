package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class CreateLinkViewModel : BasicViewModelWithRepository(){
    val domains: LiveData<List<SjDomain>> get() = repository.domains
    val tags: LiveData<List<SjTag>> get() = repository.tags
    val domainNames get() = repository.domainNames
    val selectedTags= mutableListOf<SjTag>()
    lateinit var selectedDomain: SjDomain

    fun selectDomain(position: Int) {
        selectedDomain = domains.value!![position]
    }

    fun insertLink(link:SjLink){
        repository.insertLink(selectedDomain,link,selectedTags)
    }

}