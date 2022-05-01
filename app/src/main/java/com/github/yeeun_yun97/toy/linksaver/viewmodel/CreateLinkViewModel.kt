package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

enum class NameMode {
    MODE_USER, MODE_LOAD;
}

class CreateLinkViewModel : BasicViewModelWithRepository() {
    /**repo*/
    private val networkRepository = SjNetworkRepository.newInstance()
    private val siteTitle = networkRepository.siteTitle
    val domains: LiveData<List<SjDomain>> get() = repository.domains
    val tags: LiveData<List<SjTag>> get() = repository.tags

    /**mode*/
    var mode = NameMode.MODE_LOAD  //saves link named by auto or user input

    /**data binding live data*/
    private val _fullUrl = MutableLiveData<String>()
    val fullUrl: LiveData<String> get() = _fullUrl
    val linkName = MutableLiveData<String>()
    val linkUrl = MutableLiveData<String>()

    /**Model to Save*/
    private var targetLink = SjLink(did = -1, name = "", url = "")
    private var targetDomain = SjDomain(name = "", url = "")
    val targetTagList = mutableListOf<SjTag>()
    val targetDomainData = MutableLiveData(targetDomain)

    init {
        /** auto name link by html title */
        siteTitle.observeForever {
            if (linkName.value.isNullOrEmpty() || mode == NameMode.MODE_LOAD) {
                linkName.postValue(it)
            }
        }
        fullUrl.observeForever {
            loadTitleOf(it)
        }

        /** handle user change data */
        linkName.observeForever {
            targetLink.apply { name = it }
        }
        linkUrl.observeForever {
            targetLink.apply { url = it }
        }

        /** change full url (domain.url+link.url) */
        linkUrl.observeForever {
            val url = targetDomain.url
            _fullUrl.postValue(StringBuilder(url).append(it).toString())
        }
        targetDomainData.observeForever {
            val url = targetLink.url
            _fullUrl.postValue(StringBuilder(it.url).append(url).toString())
        }
    }

    fun setLink(lid: Int) {
        viewModelScope.launch(Dispatchers.IO){
           val link =  async{repository.getLinkAndDomainWithTagsByLid(lid)}
            setLink(link.await())
        }
    }
    private fun setLink(link:SjLinksAndDomainsWithTags){
        selectLink(link.link)
        selectDomain(link.domain)
        targetTagList.clear()
        targetTagList.addAll(link.tags)
    }

    fun getSelectedDomain():SjDomain{
        return this.targetDomain;
    }

    fun selectTag(tag: SjTag) {
        targetTagList.add(tag)
    }

    fun unselectTag(tag: SjTag) {
        targetTagList.remove(tag)
    }

    fun selectDomain(position: Int) {
       val domain =domains.value!![position]
        selectDomain(domain)
    }
    private fun selectDomain(domain:SjDomain){
        targetDomain = domain
        targetDomainData.postValue(targetDomain)
    }

    fun insertLink() {
        if(targetLink.lid!=0){
            repository.updateLink(targetDomain,targetLink,targetTagList)
        }else{
            repository.insertLink(targetDomain, targetLink, targetTagList)
        }

    }

    private fun loadTitleOf(url: String) {
        networkRepository.getTitleOf(url)
    }

    private fun selectLink(link:SjLink){
        mode=NameMode.MODE_USER
        targetLink=link;
        linkName.postValue(link.name)
        linkUrl.postValue(link.url)
    }
}