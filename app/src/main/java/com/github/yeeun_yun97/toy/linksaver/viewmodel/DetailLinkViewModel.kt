package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinksAndDomainsWithTags
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjNetworkRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailLinkViewModel : BasicViewModelWithRepository() {
    private val networkRepository = SjNetworkRepository.newInstance()

    private var _link = MutableLiveData<SjLinksAndDomainsWithTags>()
    val link: LiveData<SjLinksAndDomainsWithTags> get() = _link
    private val _tags = MutableLiveData<List<SjTag>>()
    val tags: LiveData<List<SjTag>> get() = _tags
    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    fun loadLinkData(lid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getLinkAndDomainWithTagsByLid(lid)
            Log.d("------load link image", "${data.link.name}------")
            _link.postValue(data)
            _tags.postValue(data.tags)

            val fullUrl = data.domain.url + data.link.url
            launch {
                Log.d("load image from url", "url = $fullUrl")
                val imageUrl = async { networkRepository.getPreviewOf(fullUrl) }
                _imageUrl.postValue(imageUrl.await())
                Log.d("loaded image from url", "complete")
            }
        }
    }

    fun deleteLink(link: SjLink, tags: List<SjTag>) = repository.deleteLink(link, tags)

    fun clearData() {
        _link = MutableLiveData<SjLinksAndDomainsWithTags>()
        _tags.value = listOf()
        _imageUrl.value = ""
    }
}