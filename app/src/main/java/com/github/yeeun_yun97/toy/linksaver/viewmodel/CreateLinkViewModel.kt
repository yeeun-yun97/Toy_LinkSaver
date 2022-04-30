package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

enum class NameMode{
    MODE_USER, MODE_LOAD;
}

class CreateLinkViewModel : BasicViewModelWithRepository() {

    //networkRepo
    private val networkRepository = SjNetworkRepository.newInstance()
    private val siteTitle = networkRepository.siteTitle

    //roomRepo
    val domains: LiveData<List<SjDomain>> get() = repository.domains
    val tags: LiveData<List<SjTag>> get() = repository.tags

    //local
    var mode = NameMode.MODE_LOAD                                //mode saves if new link named by auto or user input
    private val selectedDomain = MutableLiveData<SjDomain>()
    private val _fullUrl = MutableLiveData<String>()
    val selectedTags = mutableListOf<SjTag>()
    val newLinkName = MutableLiveData<String>()                 //public mutable because
    val detailUrl = MutableLiveData<String>()                   //two way data binding.
    val fullUrl:LiveData<String> get()= _fullUrl

    init {
        //주소가 바뀌었을 때, 이름이 사용자가 입력한 값이 아니면,
        //이름을 사이트 주소로 설정한다.
        siteTitle.observeForever{
            if(newLinkName.value.isNullOrEmpty()||mode==NameMode.MODE_LOAD){
                newLinkName.postValue(it)
            }
        }
        detailUrl.observeForever {
            val url =
                if (selectedDomain.value != null) selectedDomain.value!!.url
                else ""
            _fullUrl.postValue(StringBuilder(url).append(it).toString())
        }
        selectedDomain.observeForever {
            val url = detailUrl.value ?: ""
            _fullUrl.postValue(StringBuilder(it.url).append(url).toString())
        }
        fullUrl.observeForever{
            loadTitleOf(it)
        }
    }

    fun selectDomain(position: Int) {
        selectedDomain.postValue(domains.value!![position])
    }

    fun insertLink() {
        val link = SjLink(
            did = selectedDomain.value!!.did,
            url = detailUrl.value!!,
            name = newLinkName.value!!
        )
        repository.insertLink(selectedDomain.value!!, link, selectedTags)
    }

    private fun loadTitleOf(url:String) {
            networkRepository.getTitleOf(url)
    }
}