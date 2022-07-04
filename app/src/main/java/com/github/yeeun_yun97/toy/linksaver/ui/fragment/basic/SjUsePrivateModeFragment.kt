package com.github.yeeun_yun97.toy.linksaver.ui.fragment.basic

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.github.yeeun_yun97.toy.linksaver.viewmodel.androidViewModels.SettingViewModel
import com.github.yeeun_yun97.toy.linksaver.viewmodel.base.SjUsePrivateModeViewModel

abstract class SjUsePrivateModeFragment<T : ViewDataBinding> : SjBasicFragment<T>() {
    private val settingViewModel : SettingViewModel by activityViewModels()

    fun applyPrivateToViewModel(viewModel:SjUsePrivateModeViewModel){
        settingViewModel.isPrivateMode.observe(viewLifecycleOwner){
            viewModel.setPrivate(it)
        }
    }
}