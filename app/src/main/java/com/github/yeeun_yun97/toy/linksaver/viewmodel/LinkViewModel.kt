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

// enum to save mode name value
enum class NameMode {
    MODE_USER, MODE_LOAD;
}

class LinkViewModel : BasicViewModelWithRepository() {
    private val networkRepository = SjNetworkRepository.newInstance()
    private val siteTitle = networkRepository.siteTitle
    val domains: LiveData<List<SjDomain>> = repository.domains
    val tags: LiveData<List<SjTag>> = repository.tags

    // mode
    var mode = NameMode.MODE_LOAD  //saves link named by auto or user input

    // data binding live data
    private val _fullUrl = MutableLiveData<String>()
    val fullUrl: LiveData<String> get() = _fullUrl
    val linkName = MutableLiveData<String>()
    val linkUrl = MutableLiveData<String>()

    // Model to Save
    private var targetLink = SjLink(did = -1, name = "", url = "")
    private var targetDomain = SjDomain(name = "", url = "")
    private val targetDomainData = MutableLiveData(targetDomain)
    val targetTagList = mutableListOf<SjTag>()

    init {
        // load auto name link by html title
        siteTitle.observeForever {
            if (linkName.value.isNullOrEmpty() || mode == NameMode.MODE_LOAD) {
                linkName.postValue(it)
            }
        }
        fullUrl.observeForever { loadTitleOf(it) }

        // handle user input change data
        linkName.observeForever { targetLink.name = it }
        linkUrl.observeForever { targetLink.url = it }

        // change full url (domain.url or link.url changes)
        linkUrl.observeForever {
            val url = targetDomain.url
            _fullUrl.postValue(StringBuilder(url).append(it).toString())
        }
        targetDomainData.observeForever {
            val url = targetLink.url
            _fullUrl.postValue(StringBuilder(it.url).append(url).toString())
        }
        /*
        //TODO
        이 부분 LiveData Transformations 활용해서 해결 가능하면 고치고 싶은 부분이다.
         */
    }

    fun initialize(){
        linkName.postValue("")
        linkUrl.postValue("")
    }



    // set link for update
    fun setLink(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val link = async { repository.getLinkAndDomainWithTagsByLid(lid) }
            setLink(link.await())
        }
    }

    private fun setLink(link: SjLinksAndDomainsWithTags) {
        selectLink(link.link)
        selectDomain(link.domain)
        targetTagList.clear()
        targetTagList.addAll(link.tags)
    }

    private fun selectLink(link: SjLink) {
        mode = NameMode.MODE_USER
        targetLink = link
        linkName.postValue(link.name)
        linkUrl.postValue(link.url)
    }


    // handle tag selection
    fun selectTag(tag: SjTag) = targetTagList.add(tag)

    fun unselectTag(tag: SjTag) = targetTagList.remove(tag)


    // fragment selection sync
    fun getSelectedDomain(): SjDomain {
        return this.targetDomain
    }

    fun selectDomain(position: Int) {
        val domain = domains.value!![position]
        selectDomain(domain)
    }

    private fun selectDomain(domain: SjDomain) {
        targetDomain = domain
        targetDomainData.postValue(targetDomain)
    }


    // save link
    fun saveLink() {
        if (targetLink.lid != 0) {
            repository.updateLinkAndTags(targetDomain, targetLink, targetTagList)
        } else {
            repository.insertLinkAndTags(targetDomain, targetLink, targetTagList)
        }

    }

    // load auto title by url
    private fun loadTitleOf(url: String) {
        networkRepository.getTitleOf(url)
    }

}