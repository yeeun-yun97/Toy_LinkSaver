package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.db.SjDatabase
import com.github.yeeun_yun97.toy.linksaver.data.model.SjLinkAndDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewLinkViewModel : ViewModel() {
    private var _linkList = MutableLiveData(ArrayList<SjLinkAndDomain>())
    val linkList: LiveData<ArrayList<SjLinkAndDomain>> get() = _linkList!!

    fun loadDatas() {
        viewModelScope.launch(Dispatchers.IO){
            val dao = SjDatabase.db.getDao()
            val links : ArrayList<SjLinkAndDomain> =  dao.getLinksAndDomain() as ArrayList<SjLinkAndDomain>
            _linkList.postValue(links)
        }
    }

}