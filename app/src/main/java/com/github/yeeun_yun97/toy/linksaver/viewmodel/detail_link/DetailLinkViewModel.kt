package com.github.yeeun_yun97.toy.linksaver.viewmodel.detail_link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailLinkViewModel : BasicViewModelWithRepository() {
    private var _link = MutableLiveData<LinkDetailValue>()
    val link: LiveData<LinkDetailValue> get() = _link

    // binding variables
    private val _bindingLinkName = MutableLiveData("")
    private val _bindingFullUrl = MutableLiveData("")
    private val _bindingTags = MutableLiveData<List<SjTag>>(listOf())
    val bindingLinkName: LiveData<String> get() = _bindingLinkName
    val bindingTags: LiveData<List<SjTag>> get() = _bindingTags
    val bindingFullUrl: LiveData<String> get() = _bindingFullUrl

    fun loadLinkData(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getLinkDetailDataByLid(lid)
            _bindingFullUrl.postValue(data.fullUrl)
            _bindingLinkName.postValue(data.name)
            _bindingTags.postValue(data.tags)
            _link.postValue(data)
        }
    }

    fun deleteLink() = repository.deleteLinkByLid(link.value!!.lid)
}