package com.github.yeeun_yun97.toy.linksaver.viewmodel.basic

import androidx.lifecycle.ViewModel
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjRepository

abstract class BasicViewModelWithRepository : ViewModel(){

    protected val repository = SjRepository.getInstance()

}