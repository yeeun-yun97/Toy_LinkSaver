package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjDomain
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLink
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditLinkViewModel(val link: SjLink) : ViewModel() {
    private var _tags = MutableLiveData<List<SjTag>>()
    private var _domains = MutableLiveData<List<SjDomain>>()
    val tags: LiveData<List<SjTag>> get() = _tags
    val domains: LiveData<List<SjDomain>> get() = _domains

    fun loadDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = SjDatabase.db.getDao()
            _tags.postValue(dao.getTags())
            _domains.postValue(dao.getDomains())
        }
    }


}