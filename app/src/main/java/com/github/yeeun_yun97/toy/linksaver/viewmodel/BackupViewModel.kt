package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.app.Application
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.github.yeeun_yun97.toy.linksaver.data.model.SjShare
import com.github.yeeun_yun97.toy.linksaver.data.repository.room.all.SjListAllRepository
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjBaseAndroidViewModelImpl
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileWriter
import java.io.OutputStream
import javax.inject.Inject


@HiltViewModel
class BackupViewModel @Inject constructor(
    application: Application,
    private val entitiesRepo: SjListAllRepository
) : SjBaseAndroidViewModelImpl(application) {
    private val groupList = entitiesRepo.groupList
    private val tagList = entitiesRepo.tagList
    private val domainList = entitiesRepo.domainList
    private val linkList = entitiesRepo.linkList

    private val groupKey = "그룹"
    private val tagKey = "태그"
    private val domainKey = "도메인"
    private val linkKey = "링크"

    private val _wrapUpList = mutableMapOf(
        groupKey to 0,
        tagKey to 0,
        domainKey to 0,
        linkKey to 0
    )
    val wrapUpList = MutableLiveData<List<Pair<String, Int>>>()

    init {
        groupList.observeForever {
            _wrapUpList[groupKey] = it.size
            wrapUpList.postValue(_wrapUpList.toList())
        }
        domainList.observeForever {
            _wrapUpList[domainKey] = it.size
            wrapUpList.postValue(_wrapUpList.toList())
        }
        linkList.observeForever {
            _wrapUpList[linkKey] = it.size
            wrapUpList.postValue(_wrapUpList.toList())
        }
        tagList.observeForever {
            _wrapUpList[tagKey] = it.size
            wrapUpList.postValue(_wrapUpList.toList())
        }
    }

    override fun refreshData() {}

    private fun collectModels(): String {
        val result = SjShare(
            groupList.value ?: listOf(),
            tagList.value ?: listOf(),
            domainList.value ?: listOf(),
            linkList.value ?: listOf()
        )
        return Gson().toJson(result)
    }

    fun open(json:String){
        val share = Gson().fromJson(json, SjShare::class.java)
        for(group in share.groups){

        }

    }

    fun write(writer: OutputStream) {
        val data = collectModels()
        writer.write(data.toByteArray())
    }
}