package com.github.yeeun_yun97.toy.linksaver.viewmodel.detail_link

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailLinkViewModel : BasicViewModelWithRepository() {
    private var _link = MutableLiveData<LinkDetailData>()
    private val _imageUrl = MutableLiveData("")
    val link: LiveData<LinkDetailData> get() = _link
    val imageUrl: LiveData<String> get() = _imageUrl
    var isVideoType : Boolean = false

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
            _link.postValue(data)
            _bindingFullUrl.postValue(data.fullUrl)
            _bindingLinkName.postValue(data.link.name)
            _bindingTags.postValue(data.tags)
            _imageUrl.postValue(data.link.preview)
            isVideoType = true
        }
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) = repository.deleteLink(link, tags)
}