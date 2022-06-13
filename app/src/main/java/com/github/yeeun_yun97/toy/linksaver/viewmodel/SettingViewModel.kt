package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SjDataStoreRepository = SjDataStoreRepository.getInstance()
    val passwordFlow = repository.getPassword(application.applicationContext)
    val privateFlow = repository.isPrivateMode(application.applicationContext)

    fun setPrivateMode(isPrivateMode: Boolean) {
        repository.setPrivateMode(getApplication<Application>().applicationContext, isPrivateMode)
    }


}