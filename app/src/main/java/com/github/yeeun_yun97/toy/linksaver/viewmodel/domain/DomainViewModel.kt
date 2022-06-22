package com.github.yeeun_yun97.toy.linksaver.viewmodel.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjDomainRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.SjBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DomainViewModel : SjBaseViewModel() {
    private val domainRepo = SjDomainRepository.getInstance()

    var did = -1
        set(value) {
            field = value
            refreshData()
        }

    // Data binding live data
    val bindingDomainName = MutableLiveData<String>()
    val bindingDomainUrl = MutableLiveData<String>()

    // Model to save
    private var targetDomain = SjDomain(name = "", url = "")


    init {
        // handle user change data
        bindingDomainName.observeForever {
            targetDomain.name = it
        }
        bindingDomainUrl.observeForever {
            targetDomain.url = it
        }
    }

    override fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val domain = async { domainRepo.selectDomainByDid(did) }
            setDomain(domain.await())
        }
    }

    private fun setDomain(domain: SjDomain) {
        targetDomain = domain
        bindingDomainName.postValue(domain.name)
        bindingDomainUrl.postValue(domain.url)
    }


    // save domain
    fun saveDomain() =
        viewModelScope.launch(Dispatchers.IO) {
            if (targetDomain.did == 0) {
                domainRepo.insertDomain(targetDomain)
            } else {
                domainRepo.updateDomain(targetDomain)
            }
        }


    //delete domain
    fun deleteDomain(domain: SjDomain) =
        viewModelScope.launch(Dispatchers.IO) {
            domainRepo.deleteDomain(domain)
        }


}