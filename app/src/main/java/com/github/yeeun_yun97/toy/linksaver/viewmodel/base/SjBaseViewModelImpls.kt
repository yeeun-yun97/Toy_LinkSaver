package com.github.yeeun_yun97.toy.linksaver.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

abstract class SjBaseViewModelImpl :
    SjBaseViewModel, ViewModel()

abstract class SjBaseAndroidViewModelImpl(application: Application) :
    SjBaseViewModel, AndroidViewModel(application)

