package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.LinkDetailValue
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.SjLinkListRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO livedata 레포로 아직 안 옮김
@HiltViewModel
class DetailLinkViewModel @Inject constructor(
    private val linkRepo : SjLinkListRepository
) : SjBaseViewModelImpl() {

    var lid: Int? = null
        set(data) {
            field = data
            refreshData()
        }

    private var _link = MutableLiveData<LinkDetailValue>()
    val link: LiveData<LinkDetailValue> get() = _link

    // binding variables
    val bindingLinkName: LiveData<String> = Transformations.map(link) { it.name }
    val bindingTags: LiveData<List<SjTag>> = Transformations.map(link) { it.tags }
    val bindingFullUrl: LiveData<String> = Transformations.map(link) { it.fullUrl }


    override fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = linkRepo.selectLinkValueByLid(lid!!)
            _link.postValue(data)
        }
    }

    fun deleteLink() {
        linkRepo.deleteLinkByLid(lid!!)
    }


}