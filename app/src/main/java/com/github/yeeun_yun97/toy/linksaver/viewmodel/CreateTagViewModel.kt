package com.github.yeeun_yun97.toy.linksaver.viewmodel

import com.github.yeeun_yun97.toy.linksaver.data.model.SjTag
import com.github.yeeun_yun97.toy.linksaver.viewmodel.basic.BasicViewModelWithRepository

class CreateTagViewModel : BasicViewModelWithRepository(){

    fun insertTag(newTag: SjTag) {
        repository.insertTag(newTag)
    }

}