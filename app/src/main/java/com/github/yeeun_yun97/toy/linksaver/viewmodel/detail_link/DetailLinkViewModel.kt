package com.github.yeeun_yun97.toy.linksaver.viewmodel.detail_link

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkModelUtil
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailLinkViewModel : BasicViewModelWithRepository() {
    private var _link = MutableLiveData<SjLinksAndDomainsWithTags>()
    private val _imageUrl = MutableLiveData<String>("")
    val link: LiveData<SjLinksAndDomainsWithTags> get() = _link
    val imageUrl: LiveData<String> get() = _imageUrl

    // binding variables
    private val _bindingLinkName = MutableLiveData("")
    private val _bindingFullUrl = MutableLiveData("")
    private val _bindingTags = MutableLiveData<List<SjTag>>(listOf())
    val bindingLinkName: LiveData<String> get() = _bindingLinkName
    val bindingTags: LiveData<List<SjTag>> get() = _bindingTags
    val bindingFullUrl: LiveData<String> get() = _bindingFullUrl

    fun loadLinkData(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getLinkAndDomainWithTagsByLid(lid)
            _link.postValue(data)
            _imageUrl.postValue(data.link.preview)
            _bindingLinkName.postValue(data.link.name)
            _bindingFullUrl.postValue(LinkModelUtil.getFullUrl(data))
            _bindingTags.postValue(data.tags)
        }
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) = repository.deleteLink(link, tags)
}