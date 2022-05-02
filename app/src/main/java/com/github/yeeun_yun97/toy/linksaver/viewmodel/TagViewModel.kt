package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TagViewModel : BasicViewModelWithRepository(){

    /** repo */
    val tags = repository.tags

    /** data binding live data */
    val tagName = MutableLiveData<String>()

    /** Model to save */
    private var targetTag = SjTag(name="")

    init{
        /** handle user change data */
        tagName.observeForever{
            targetTag.name=it
            Log.d("TagName change", it)
        }
    }

    fun setTag(tid:Int){
        viewModelScope.launch(Dispatchers.IO){
            val tag = async{repository.getTagByTid(tid)}
            setTag(tag.await())
        }
    }

    private fun setTag(tag:SjTag){
        targetTag=tag
        tagName.postValue(tag.name)
    }

    fun saveTag(){
        if(targetTag.tid==0){
            insertTag()
        }else{
            updateTag()
        }
    }

    private fun insertTag() {
        repository.insertTag(targetTag)
    }

    private fun updateTag(){
        repository.updateTag(targetTag)
    }

    fun deleteTag(tag:SjTag){
        repository.deleteTag(tag)
    }



}