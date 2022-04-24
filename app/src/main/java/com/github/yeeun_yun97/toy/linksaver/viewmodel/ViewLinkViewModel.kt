package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkAndDomain
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewLinkViewModel : ViewModel() {
    private var _linkList = MutableLiveData(ArrayList<SjLinkAndDomain>())
    private var _domainNames = MutableLiveData<MutableList<String>>()
    private val _domainUrls = MutableLiveData<MutableList<String>>()

    val linkList: LiveData<ArrayList<SjLinkAndDomain>> get() = _linkList
    val domainNames: LiveData<MutableList<String>> get() = _domainNames
    val domainUrls: LiveData<MutableList<String>> get() = _domainUrls

    fun loadDatas() {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = SjDatabase.getDao()
            val links: ArrayList<SjLinkAndDomain> =
                dao.getLinksAndDomain() as ArrayList<SjLinkAndDomain>
            _linkList.postValue(links)
        }
    }

    fun loadDomainNames() {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = SjDatabase.getDao()
            val domainList = dao.getDomains()


            val nameList: MutableList<String> = mutableListOf()
            val urlList:MutableList<String> = mutableListOf()
            for (domain in domainList) {
                nameList.add(0,domain.name)
                urlList.add(0,domain.url)
            }
            _domainUrls.postValue(urlList)
            _domainNames.postValue(nameList)



        }
    }
}