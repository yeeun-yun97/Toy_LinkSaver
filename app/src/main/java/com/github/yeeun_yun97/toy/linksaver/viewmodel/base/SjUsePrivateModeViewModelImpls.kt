package com.github.yeeun_yun97.toy.linksaver.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

abstract class SjUsePrivateModeViewModelAndroidImpl(application: Application) :
    SjUsePrivateModeViewModel, AndroidViewModel(application) {
    protected var isPrivateMode: Boolean = false
        set(value) {
            field = value
            refreshData()
        }

    override fun setPrivate(isPrivateMode: Boolean) {
        this.isPrivateMode = isPrivateMode
    }
}

abstract class SjUsePrivateModeViewModelImpl :
    SjUsePrivateModeViewModel, ViewModel() {
    protected var isPrivateMode: Boolean = false
        set(value) {
            field = value
            refreshData()
        }

    override fun setPrivate(isPrivateMode: Boolean) {
        this.isPrivateMode = isPrivateMode
    }
}