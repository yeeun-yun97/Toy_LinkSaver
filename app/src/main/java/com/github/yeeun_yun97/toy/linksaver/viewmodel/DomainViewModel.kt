package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DomainViewModel : BasicViewModelWithRepository() {
    val domains = repository.domainsExceptDefault

    // Data binding live data
    val domainName = MutableLiveData<String>()
    val domainUrl = MutableLiveData<String>()

    // Model to save
    private var targetDomain = SjDomain(name = "", url = "")


    init {
        // handle user change data
        domainName.observeForever {
            targetDomain.name = it
        }
        domainUrl.observeForever {
            targetDomain.url = it
        }
    }


    // set domain to update
    fun setDomain(did: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val domain = async { repository.getDomainByDid(did) }
            setDomain(domain.await())
        }
    }

    private fun setDomain(domain: SjDomain) {
        targetDomain = domain
        domainName.postValue(domain.name)
        domainUrl.postValue(domain.url)
    }


    // save domain
    fun saveDomain() {
        if (targetDomain.did == 0) {
            repository.insertDomain(targetDomain)
        } else {
            repository.updateDomain(targetDomain)
        }
    }


    //delete domain
    fun deleteDomain(domain: SjDomain) = repository.deleteDomain(domain)

}