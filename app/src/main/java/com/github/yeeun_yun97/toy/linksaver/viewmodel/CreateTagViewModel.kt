package com.github.yeeun_yun97.toy.linksaver.viewmodel

import androidx.lifecycle.ViewModel
import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjRepository

class CreateTagViewModel : ViewModel(){
    private val repository = SjRepository()

    fun insertTag(newTag: SjTag) {
        repository.insertTag(newTag)
    }
}