package com.github.yeeun_yun97.toy.linksaver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.github.yeeun_yun97.toy.linksaver.data.repository.SjDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    application: Application,
    private val repository: SjDataStoreRepository
) : AndroidViewModel(application) {
    val passwordFlow = repository.getPassword(application.applicationContext)
    val privateFlow = repository.isPrivateMode(application.applicationContext)
    val isPrivateMode: LiveData<Boolean> = privateFlow.asLiveData()

    fun setPrivateMode(isPrivateMode: Boolean) {
        repository.setPrivateMode(getApplication<Application>().applicationContext, isPrivateMode)
    }


}