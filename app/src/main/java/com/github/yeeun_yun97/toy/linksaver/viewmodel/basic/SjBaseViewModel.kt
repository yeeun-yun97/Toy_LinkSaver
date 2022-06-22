package com.github.yeeun_yun97.toy.linksaver.viewmodel.basic

import androidx.lifecycle.ViewModel

abstract class SjBaseViewModel : ViewModel() {

    protected var isPrivateMode: Boolean = false

    abstract fun refreshData()

}