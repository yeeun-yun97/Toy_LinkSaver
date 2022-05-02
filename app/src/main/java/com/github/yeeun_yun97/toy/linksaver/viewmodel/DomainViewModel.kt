package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DomainViewModel : BasicViewModelWithRepository() {
    /** Repo */
    val domains = repository.domains

    /** Data binding live data */
    val domainName = MutableLiveData<String>()
    val domainUrl = MutableLiveData<String>()

    /** Model to save */
    private var targetDomain = SjDomain(name = "", url = "")

    init {
        /** handle user change data */
        domainName.observeForever {
            targetDomain.name = it
            Log.d("DomainName change", it)
        }
        domainUrl.observeForever {
            targetDomain.url = it
            Log.d("DomainUrl change", it)
        }
    }

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

    fun saveDomain() {
        if (targetDomain.did == 0) {
            insertDomain()
        } else {
            updateDomain()
        }
    }

    private fun insertDomain() {
        repository.insertDomain(targetDomain)
        Log.d("insertDomain",targetDomain.toString())
    }

    private fun updateDomain() {
        repository.updateDomain(targetDomain)
        Log.d("updateDomain",targetDomain.toString())
    }

    fun deleteDomain(domain: SjDomain) {
        repository.deleteDomain(domain)
        //링크에서 도메인을 참조하고 있을 수 있으니,
        //확인하고, 있으면 지우는 작업을 하지 않고,
        //사용자에게 알릴 수 있으면 좋겠다.
    }
}